package no.fint.graphql;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import graphql.GraphQLContext;
import graphql.schema.DataFetchingEnvironment;
import io.netty.channel.ConnectTimeoutException;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.WriteTimeoutException;
import lombok.extern.slf4j.Slf4j;
import no.fint.graphql.config.ConnectionProviderSettings;
import no.fint.graphql.dataloader.ResourceRequestKey;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;
import reactor.core.publisher.Mono;
import reactor.netty.internal.shaded.reactor.pool.PoolAcquirePendingLimitException;
import reactor.netty.internal.shaded.reactor.pool.PoolAcquireTimeoutException;
import reactor.util.retry.Retry;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

@Slf4j
@Component
public class WebClientRequest {

    private static final Duration POOL_ACQUIRE_RETRY_DELAY = Duration.ofMillis(25);
    private static final long POOL_ACQUIRE_RETRY_ATTEMPTS = 400;
    private static final String ORG_ID_HEADER = "x-org-id";
    private static final String MODEL_VERSION_HEADER = "x-fint-model-version";

    private final String modelVersion;
    private final String hostHeader;
    private final WebClient webClient;
    private final UriRouteCatalog routeCatalog;
    private final URI rootUri;
    private final Cache<HashCode, Object> cache;
    private final HashFunction hashFunction;
    private final int maxConcurrentRequests;
    private final AsyncPermitLimiter inFlightLimiter;
    private final long acquireTimeoutMs;
    private final GraphQLQueryIdProvider queryIdProvider;

    public WebClientRequest(
            WebClient webClient,
            ConnectionProviderSettings connectionProviderSettings,
            @Value("${fint.endpoint.root:http://traefik.traefik-v2}") String rootUrl,
            @Value("${fint.endpoint.host:beta.felleskomponent.no}") String hostHeader,
            @Value("${fint.webclient.cache-spec:maximumSize=10000,expireAfterWrite=10m}") String cacheSpec,
            @Value("${fint.graphql.model-version:V4}") String modelVersion,
            GraphQLQueryIdProvider queryIdProvider,
            UriRouteCatalog routeCatalog) {
        this.modelVersion = modelVersion;
        this.hostHeader = hostHeader;
        this.webClient = webClient;
        this.routeCatalog = routeCatalog;
        this.rootUri = URI.create(StringUtils.appendIfMissing(rootUrl, "/"));
        cache = Caffeine.from(cacheSpec).build();
        this.queryIdProvider = queryIdProvider;
        hashFunction = Hashing.murmur3_128();
        maxConcurrentRequests = Math.max(1, connectionProviderSettings.getMaxConnections());
        inFlightLimiter = new AsyncPermitLimiter(maxConcurrentRequests);
        acquireTimeoutMs = connectionProviderSettings.getAcquireTimeout();

        log.info("WebClient limits: maxConnections={}, acquireMaxCount={}, effectiveAcquireMaxCount={}, acquireTimeoutMs={}",
                connectionProviderSettings.getMaxConnections(),
                connectionProviderSettings.getAcquireMaxCount(),
                connectionProviderSettings.getEffectiveAcquireMaxCount(),
                acquireTimeoutMs);
    }

    /** Variables are raw path-segment values, not already encoded link fragments. */
    public <T> Mono<T> get(String template, Map<String, String> variables, Class<T> type,
                           DataFetchingEnvironment dfe) {
        Map<String, String> encoded = new HashMap<>();
        variables.forEach((key, value) -> encoded.put(key,
                UriUtils.encodePathSegment(value, StandardCharsets.UTF_8)));
        String uri = UriComponentsBuilder.fromUriString(template).build().expand(encoded).toUriString();
        return get(uri, type, dfe);
    }

    public <T> Mono<T> get(String uri, Class<T> type, DataFetchingEnvironment dfe) {
        String cacheKeyUri = normalizeRequestUri(uri);
        GraphQLQueryContext query = queryContext(dfe);
        return Mono.defer(() -> {
            if (query.isFinished()) {
                return Mono.empty();
            }
            ResourceRequestKey key = new ResourceRequestKey(cacheKeyUri, type, query.getAuthorization());
            Mono<Object> mono = query.getRequestCache().get(key,
                    ignored -> Mono.defer(() -> getDirect(uri, type, query))
                            .takeUntilOther(query.completion())
                            .cast(Object.class)
                            .cache());
            return mono.cast(type);
        });
    }

    /** Same access checks and limits as {@link #get}, for responses too large to keep for the whole query. */
    public <T> Mono<T> getUncached(String uri, Class<T> type, DataFetchingEnvironment dfe) {
        GraphQLQueryContext query = queryContext(dfe);
        return Mono.defer(() -> query.isFinished()
                ? Mono.<T>empty()
                : getDirect(uri, type, query).takeUntilOther(query.completion()));
    }

    /** Makes later {@link #get} calls for this uri and type in the same query answer with the value. */
    public <T> void primeQueryCache(String uri, Class<T> type, T value, DataFetchingEnvironment dfe) {
        GraphQLQueryContext query = queryContext(dfe);
        ResourceRequestKey key = new ResourceRequestKey(normalizeRequestUri(uri), type, query.getAuthorization());
        query.getRequestCache().put(key, Mono.just(value));
    }

    /** Runs work once per key within a query. Later callers wait for the same run, and it stops with the query. */
    public Mono<Void> oncePerQuery(String key, Class<?> type, Supplier<Mono<Void>> work, DataFetchingEnvironment dfe) {
        GraphQLQueryContext query = queryContext(dfe);
        ResourceRequestKey cacheKey = new ResourceRequestKey(key, type, query.getAuthorization());
        return query.getRequestCache().get(cacheKey,
                        ignored -> Mono.defer(work)
                                .takeUntilOther(query.completion())
                                .cast(Object.class)
                                .cache())
                .then();
    }

    private GraphQLQueryContext queryContext(DataFetchingEnvironment dfe) {
        GraphQLContext context = dfe != null ? dfe.getGraphQlContext() : null;
        GraphQLQueryContext query = context != null ? context.get(GraphQLQueryContext.class) : null;
        if (query == null) {
            throw new MissingAuthorizationException("Missing query context");
        }
        return query;
    }

    private <T> Mono<T> getDirect(String uri, Class<T> type, GraphQLQueryContext query) {
        Long queryId = query.getQueryId();
        if (queryId == null) {
            queryId = queryIdProvider.nextId();
        }
        long requestSequence = query.nextRequestSequence();
        String queryIdValue = formatQueryId(queryId);
        String requestIdValue = formatRequestId(requestSequence);
        long requestStartNanos = System.nanoTime();

        String token = query.getAuthorization();
        if (StringUtils.isBlank(token)) {
            throw new MissingAuthorizationException("Missing Authorization token");
        }
        String organisationId = query.getOrganisationId();
        if (StringUtils.isBlank(organisationId)) {
            throw new MissingAuthorizationException("Missing organisation id");
        }

        String resourcePath = getResourcePath(uri);
        if (!isAuthorized(resourcePath, query.getAllowedPathPrefixes())) {
            throw new UnauthorizedResourceAccessException("Forbidden", uri, queryIdValue, requestIdValue);
        }

        URI resolvedUri = toRequestUri(uri);
        final WebClient.RequestHeadersSpec<?> webClientRequest = webClient.get()
                .uri(routeCatalog.template(resolvedUri), ignored -> resolvedUri);
        webClientRequest.header(HttpHeaders.AUTHORIZATION, token);
        webClientRequest.header(HttpHeaders.HOST, hostHeader);
        webClientRequest.header(ORG_ID_HEADER, organisationId);
        webClientRequest.header(MODEL_VERSION_HEADER, modelVersion);
        if (StringUtils.containsIgnoreCase(resourcePath, "/kodeverk/")) {
            HashCode key = hashFunction.newHasher().putUnencodedChars(token).putUnencodedChars(uri).hash();
            final T result = (T) cache.getIfPresent(key);
            if (result != null) {
                log.trace("Cache hit on {}", uri);
                return Mono.just(result);
            }
            log.trace("Cache miss on {}", uri);
            return get(webClientRequest, type, queryIdValue, requestIdValue, uri, requestStartNanos, query)
                    .doOnNext(value -> cache.put(key, value));
        }
        return get(webClientRequest, type, queryIdValue, requestIdValue, uri, requestStartNanos, query);
    }

    private <T> Mono<T> get(WebClient.RequestHeadersSpec<?> request, Class<T> type,
                            String queryIdValue, String requestIdValue, String uri, long requestStartNanos,
                            GraphQLQueryContext query) {
        return withPermit(
                Mono.defer(() -> {
                    if (query.isFinished()) {
                        return Mono.empty();
                    }
                    long nettyStartNanos = System.nanoTime();
                    AtomicInteger responseStatusCode = new AtomicInteger(-1);
                    return request.exchangeToMono(response -> {
                                responseStatusCode.set(response.statusCode().value());
                                if (response.statusCode().isError()) {
                                    return response.createException().flatMap(Mono::error);
                                }
                                return response.bodyToMono(type);
                            })
                            .retryWhen(Retry.fixedDelay(POOL_ACQUIRE_RETRY_ATTEMPTS, POOL_ACQUIRE_RETRY_DELAY)
                                    .filter(this::isPoolAcquireFailure)
                                    .onRetryExhaustedThrow((spec, signal) -> signal.failure()))
                            .onErrorResume(WebClientResponseException.class, ex -> {
                                log.warn("WebClient response error: GET {} -> {}, queryId={}, requestId={}, Cause={}", uri, ex.getStatusCode(), queryIdValue, requestIdValue, ex.getMessage());
                                return Mono.error(ex);
                            })
                            .onErrorMap(ex -> {
                                if (ex instanceof WebClientResponseException responseException) {
                                    log.warn("WebClient response error: GET {} -> {}, queryId={}, requestId={}, Cause={}", uri, responseException.getStatusCode(), queryIdValue, requestIdValue, ex.getMessage());
                                    return ex;
                                } else if (ex instanceof org.springframework.web.reactive.function.client.WebClientRequestException requestException) {
                                    log.warn("WebClient request error: {} {}, queryId={}, requestId={}, Cause={}", requestException.getMethod(), requestException.getUri(), queryIdValue, requestIdValue, ex.getMessage());
                                }
                                return new WebClientRequestException(
                                        "WebClient request failed",
                                        ex,
                                        uri,
                                        queryIdValue,
                                        requestIdValue
                                );
                            })
                            .doOnError(ex -> {
                                if (ex instanceof WebClientResponseException) {
                                    return;
                                }
                                logRequestFailure(ex, queryIdValue, requestIdValue, uri);
                            })
                            .doFinally(signal -> logNettyRequestEnd(
                                    queryIdValue,
                                    requestIdValue,
                                    uri,
                                    requestStartNanos,
                                    nettyStartNanos,
                                    responseStatusCode.get()
                            ));
                })
        );
    }

    private <T> Mono<T> withPermit(Mono<T> mono) {
        return Mono.usingWhen(
                inFlightLimiter.acquire(),
                ignored -> mono,
                AsyncPermitLimiter.Permit::release,
                (permit, error) -> permit.release(),
                AsyncPermitLimiter.Permit::release
        );
    }

    private boolean isAuthorized(String resourcePath, Set<String> allowedPathPrefixes) {
        if (StringUtils.isBlank(resourcePath)) {
            return false;
        }
        if (allowedPathPrefixes.isEmpty()) {
            return false;
        }
        String normalizedPath = normalizePath(resourcePath);
        for (String allowedPathPrefix : allowedPathPrefixes) {
            String normalizedPrefix = trimTrailingSlash(normalizePath(allowedPathPrefix));
            if ("/".equals(normalizedPrefix)) {
                return true;
            }
            if (normalizedPath.equals(normalizedPrefix) || normalizedPath.startsWith(normalizedPrefix + "/")) {
                return true;
            }
        }
        return false;
    }

    private String getResourcePath(String uri) {
        if (StringUtils.isBlank(uri)) {
            return null;
        }
        try {
            String path = URI.create(uri).getPath();
            return StringUtils.isNotBlank(path) ? path : uri;
        } catch (IllegalArgumentException ex) {
            return uri;
        }
    }

    private String normalizeRequestUri(String uri) {
        if (StringUtils.isBlank(uri)) {
            return uri;
        }
        try {
            URI parsed = URI.create(uri);
            if (!parsed.isAbsolute()) {
                return uri;
            }
            String path = StringUtils.defaultIfBlank(parsed.getRawPath(), "/");
            if (StringUtils.isNotBlank(parsed.getRawQuery())) {
                return path + "?" + parsed.getRawQuery();
            }
            return path;
        } catch (IllegalArgumentException ex) {
            return uri;
        }
    }

    private String normalizePath(String path) {
        String normalized = StringUtils.defaultString(path).trim();
        if (normalized.isEmpty()) {
            return normalized;
        }
        normalized = normalized.startsWith("/") ? normalized : "/" + normalized;
        return normalized.replaceAll("/{2,}", "/");
    }

    private String trimTrailingSlash(String path) {
        if ("/".equals(path)) {
            return path;
        }
        return StringUtils.removeEnd(path, "/");
    }

    private URI toRequestUri(String uri) {
        if (StringUtils.isBlank(uri)) {
            return rootUri;
        }
        URI parsed = parseAndEncode(uri);
        if (parsed.isAbsolute()) {
            return rootUri.resolve(toPathAndQuery(parsed));
        }
        String pathAndQuery = toPathAndQuery(parsed);
        return rootUri.resolve(pathAndQuery.startsWith("/") ? pathAndQuery : "/" + pathAndQuery);
    }

    private URI parseAndEncode(String uri) {
        try {
            return URI.create(uri);
        } catch (IllegalArgumentException ex) {
            String encoded = UriComponentsBuilder.fromUriString(uri)
                    .build()
                    .encode()
                    .toUriString();
            // UriComponentsBuilder cannot know that valid escapes in a partly raw URI
            // are already encoded. Restore exactly those escapes after encoding the raw characters.
            return URI.create(encoded.replaceAll("(?i)%25(?=[0-9a-f]{2})", "%"));
        }
    }

    private String toPathAndQuery(URI uri) {
        String path = StringUtils.defaultIfBlank(uri.getRawPath(), "/");
        if (StringUtils.isNotBlank(uri.getRawQuery())) {
            return path + "?" + uri.getRawQuery();
        }
        return path;
    }

    public int getMaxConcurrentRequests() {
        return maxConcurrentRequests;
    }

    public long getReferenceDataCacheEstimatedSize() {
        return cache.estimatedSize();
    }

    public int getAvailableConcurrencyPermits() {
        return inFlightLimiter.getAvailablePermits();
    }

    public int getQueuedConcurrencyWaiters() {
        return inFlightLimiter.getQueueLength();
    }

    private void logNettyRequestEnd(
            String queryIdValue,
            String requestIdValue,
            String uri,
            long requestStartNanos,
            long nettyStartNanos,
            int responseStatusCode) {
        long totalDurationMs = (System.nanoTime() - requestStartNanos) / 1_000_000;
        long queueDurationMs = (nettyStartNanos - requestStartNanos) / 1_000_000;
        long nettyDurationMs = totalDurationMs - queueDurationMs;
        String responseCodeValue = responseStatusCode >= 0 ? Integer.toString(responseStatusCode) : "n/a";
        log.debug("WebClient request queryId={} requestId={} statusCode={} queueDurationMs={} nettyDurationMs={} totalDurationMs={} uri={}",
                queryIdValue, requestIdValue, responseCodeValue, queueDurationMs, nettyDurationMs, totalDurationMs, uri);
    }

    private void logRequestFailure(Throwable exception, String queryIdValue, String requestIdValue, String uri) {
        Throwable root = findRelevantCause(exception);
        if (root instanceof PoolAcquireTimeoutException || root instanceof PoolAcquirePendingLimitException) {
            log.warn("WebClient connection pool exhausted (pending acquire timeout). queryId={} requestId={} uri={} {}",
                    queryIdValue, requestIdValue, uri, root.getMessage());
            return;
        }
        if (root instanceof ConnectTimeoutException) {
            log.warn("WebClient connect timeout. queryId={} requestId={} uri={} {}",
                    queryIdValue, requestIdValue, uri, root.getMessage());
            return;
        }
        if (root instanceof ReadTimeoutException || root instanceof WriteTimeoutException) {
            log.warn("WebClient response timeout. queryId={} requestId={} uri={} {}",
                    queryIdValue, requestIdValue, uri, root.getMessage());
            return;
        }
        log.error("WebClient request error: queryId={} requestId={} uri={} {}",
                queryIdValue, requestIdValue, uri, root.getMessage(), root);
    }

    private Throwable findRelevantCause(Throwable exception) {
        Throwable current = exception;
        while (current != null) {
            if (current instanceof PoolAcquireTimeoutException
                    || current instanceof PoolAcquirePendingLimitException
                    || current instanceof ConnectTimeoutException
                    || current instanceof ReadTimeoutException
                    || current instanceof WriteTimeoutException) {
                return current;
            }
            current = current.getCause();
        }
        return exception;
    }

    private boolean isPoolAcquireFailure(Throwable exception) {
        Throwable root = findRelevantCause(exception);
        return root instanceof PoolAcquireTimeoutException || root instanceof PoolAcquirePendingLimitException;
    }

    private String formatQueryId(Long queryId) {
        return queryId != null ? queryId.toString() : "unknown";
    }

    private String formatRequestId(long requestSequence) {
        return requestSequence > 0 ? Long.toString(requestSequence) : "unknown";
    }
}
