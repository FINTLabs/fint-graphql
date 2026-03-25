package no.fint.graphql;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import graphql.GraphQLContext;
import graphql.kickstart.execution.context.GraphQLKickstartContext;
import graphql.schema.DataFetchingEnvironment;
import io.netty.channel.ConnectTimeoutException;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.WriteTimeoutException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import no.fint.graphql.config.ConnectionProviderSettings;
import no.fint.graphql.dataloader.ResourceRequestKey;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.netty.internal.shaded.reactor.pool.PoolAcquirePendingLimitException;
import reactor.netty.internal.shaded.reactor.pool.PoolAcquireTimeoutException;
import reactor.util.retry.Retry;

import java.net.URI;
import java.time.Duration;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class WebClientRequest {

    private static final Duration POOL_ACQUIRE_RETRY_DELAY = Duration.ofMillis(25);
    private static final long POOL_ACQUIRE_RETRY_ATTEMPTS = 400;
    private static final String ORG_ID_HEADER = "x-org-id";

    private final WebClient webClient;
    private final Cache<HashCode, Object> cache;
    private final HashFunction hashFunction;
    private final int maxConcurrentRequests;
    private final AsyncPermitLimiter inFlightLimiter;
    private final long acquireTimeoutMs;
    private final Cache<String, Cache<ResourceRequestKey, Mono<Object>>> requestScopedLookups;
    private final GraphQLQueryIdProvider queryIdProvider;
    private final AtomicLong fallbackRequestCounter = new AtomicLong();

    public WebClientRequest(
            WebClient webClient,
            ConnectionProviderSettings connectionProviderSettings,
            @Value("${fint.webclient.cache-spec:maximumSize=10000,expireAfterWrite=10m}") String cacheSpec,
            GraphQLQueryIdProvider queryIdProvider) {
        this.webClient = webClient;
        cache = Caffeine.from(cacheSpec).build();
        this.queryIdProvider = queryIdProvider;
        hashFunction = Hashing.murmur3_128();
        maxConcurrentRequests = Math.max(1, connectionProviderSettings.getMaxConnections());
        inFlightLimiter = new AsyncPermitLimiter(maxConcurrentRequests);
        acquireTimeoutMs = connectionProviderSettings.getAcquireTimeout();
        requestScopedLookups = Caffeine.newBuilder()
                .maximumSize(10000)
                .expireAfterWrite(Duration.ofMinutes(5))
                .build();

        log.info("WebClient limits: maxConnections={}, acquireMaxCount={}, effectiveAcquireMaxCount={}, acquireTimeoutMs={}",
                connectionProviderSettings.getMaxConnections(),
                connectionProviderSettings.getAcquireMaxCount(),
                connectionProviderSettings.getEffectiveAcquireMaxCount(),
                acquireTimeoutMs);
    }

    public <T> Mono<T> get(String uri, Class<T> type, DataFetchingEnvironment dfe) {
        GraphQLKickstartContext context = getContext(dfe);
        HttpServletRequest request = getRequest(context);
        String authorization = getToken(request);
        String requestScope = getRequestScope(dfe, request);
        if (requestScope == null) {
            return getDirect(uri, type, request, authorization);
        }
        ResourceRequestKey key = new ResourceRequestKey(uri, type, authorization);
        Cache<ResourceRequestKey, Mono<Object>> requestCache = requestScopedLookups.get(
                requestScope,
                ignored -> Caffeine.newBuilder().maximumSize(10000).build()
        );
        Mono<Object> mono = requestCache.get(key,
                ignored -> getDirect(uri, type, request, authorization)
                        .cast(Object.class)
                        .cache());
        return mono.cast(type);
    }

    private <T> Mono<T> getDirect(String uri, Class<T> type, HttpServletRequest request, String authorization) {
        Long queryId = GraphQLRequestAttributes.getQueryId(request);
        if (queryId == null) {
            queryId = queryIdProvider.nextId();
        }
        long requestSequence = GraphQLRequestAttributes.nextRequestSequence(request);
        if (requestSequence < 1) {
            requestSequence = fallbackRequestCounter.incrementAndGet();
        }
        String queryIdValue = formatQueryId(queryId);
        String requestIdValue = formatRequestId(requestSequence);
        long requestStartNanos = System.nanoTime();

        String token = getToken(request);
        if (token == null) {
            token = authorization;
        }
        if (StringUtils.isBlank(token)) {
            throw new MissingAuthorizationException("Missing Authorization token");
        }
        String organisationId = GraphQLRequestAttributes.getOrganisationId(request);
        if (StringUtils.isBlank(organisationId)) {
            throw new MissingAuthorizationException("Missing organisation id");
        }

        String resourcePath = getResourcePath(uri);
        if (!isAuthorized(resourcePath, request)) {
            throw new UnauthorizedResourceAccessException("Unauthorized", uri, queryIdValue, requestIdValue);
        }

        final WebClient.RequestHeadersSpec<?> webClientRequest = webClient.get().uri(uri);
        webClientRequest.header(HttpHeaders.AUTHORIZATION, token);
        webClientRequest.header(ORG_ID_HEADER, organisationId);
        if (StringUtils.containsIgnoreCase(resourcePath, "/kodeverk/")) {
            HashCode key = hashFunction.newHasher().putUnencodedChars(token).putUnencodedChars(uri).hash();
            final T result = (T) cache.getIfPresent(key);
            if (result != null) {
                log.trace("Cache hit on {}", uri);
                return Mono.just(result);
            }
            log.trace("Cache miss on {}", uri);
            return get(webClientRequest, type, queryIdValue, requestIdValue, uri, requestStartNanos)
                    .doOnNext(value -> cache.put(key, value));
        }
        return get(webClientRequest, type, queryIdValue, requestIdValue, uri, requestStartNanos);
    }

    private <T> Mono<T> get(WebClient.RequestHeadersSpec<?> request, Class<T> type,
                            String queryIdValue, String requestIdValue, String uri, long requestStartNanos) {
        return withPermit(
                Mono.defer(() -> {
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

    private String getToken(HttpServletRequest request) {
        return request != null ? request.getHeader(HttpHeaders.AUTHORIZATION) : null;
    }

    private String getRequestScope(DataFetchingEnvironment dfe, HttpServletRequest request) {
        if (dfe != null && dfe.getExecutionId() != null) {
            return dfe.getExecutionId().toString();
        }
        Long queryId = GraphQLRequestAttributes.getQueryId(request);
        if (queryId != null) {
            return "query:" + queryId;
        }
        return null;
    }

    private boolean isAuthorized(String resourcePath, HttpServletRequest request) {
        if (StringUtils.isBlank(resourcePath)) {
            return false;
        }
        Set<String> allowedPathPrefixes = GraphQLRequestAttributes.getAllowedPathPrefixes(request);
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

    public int getMaxConcurrentRequests() {
        return maxConcurrentRequests;
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

    private GraphQLKickstartContext getContext(DataFetchingEnvironment dataFetchingEnvironment) {
        if (dataFetchingEnvironment == null) {
            return null;
        }
        GraphQLContext graphQLContext = dataFetchingEnvironment.getGraphQlContext();
        if (graphQLContext == null) {
            return null;
        }
        HashMap<Object, Object> contextMap = new HashMap<>();
        graphQLContext.stream().forEach(entry -> contextMap.put(entry.getKey(), entry.getValue()));
        return GraphQLKickstartContext.of(contextMap);
    }

    private HttpServletRequest getRequest(GraphQLKickstartContext context) {
        if (context == null || context.getMapOfContext() == null) {
            return null;
        }
        Object request = context.getMapOfContext().get(HttpServletRequest.class);
        return request instanceof HttpServletRequest ? (HttpServletRequest) request : null;
    }
}
