package no.fint.graphql;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import graphql.schema.DataFetchingEnvironment;
import graphql.servlet.context.GraphQLServletContext;
import io.netty.channel.ConnectTimeoutException;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.WriteTimeoutException;
import lombok.extern.slf4j.Slf4j;
import no.fint.graphql.config.ConnectionProviderSettings;
import no.fint.graphql.dataloader.ResourceDataLoader;
import no.fint.graphql.dataloader.ResourceRequestKey;
import org.apache.commons.lang3.StringUtils;
import org.dataloader.DataLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.netty.internal.shaded.reactor.pool.PoolAcquirePendingLimitException;
import reactor.netty.internal.shaded.reactor.pool.PoolAcquireTimeoutException;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Component
public class WebClientRequest {

    private final WebClient webClient;
    private final Cache<HashCode, Object> cache;
    private final HashFunction hashFunction;
    private final BlacklistService blacklistService;
    private final Set<String> remoteAddresses = new HashSet<>();
    private final Semaphore concurrencyLimiter;
    private final Cache<String, DataLoader<ResourceRequestKey, Object>> requestScopedLoaders;
    private final GraphQLQueryIdProvider queryIdProvider;
    private final AtomicLong fallbackRequestCounter = new AtomicLong();

    public WebClientRequest(
            WebClient webClient,
            ConnectionProviderSettings connectionProviderSettings,
            @Value("${fint.webclient.cache-spec:maximumSize=10000,expireAfterWrite=10m}") String cacheSpec,
            BlacklistService blacklistService,
            GraphQLQueryIdProvider queryIdProvider) {
        this.webClient = webClient;
        cache = Caffeine.from(cacheSpec).build();
        this.blacklistService = blacklistService;
        this.queryIdProvider = queryIdProvider;
        hashFunction = Hashing.murmur3_128();
        concurrencyLimiter = new Semaphore(connectionProviderSettings.getMaxConnections(), true);
        requestScopedLoaders = Caffeine.newBuilder()
                .maximumSize(10000)
                .expireAfterWrite(Duration.ofMinutes(5))
                .build();

        log.info("Max outgoing HTTP connections: {}", connectionProviderSettings.getMaxConnections());
    }

    public <T> Mono<T> get(String uri, Class<T> type, DataFetchingEnvironment dfe) {
        GraphQLServletContext context = getContext(dfe);
        String authorization = getToken(context);
        DataLoader<ResourceRequestKey, Object> dataLoader = getDataLoader(dfe);
        boolean manualDispatch = false;
        if (dataLoader == null) {
            dataLoader = getRequestScopedDataLoader(dfe, context);
            manualDispatch = dataLoader != null;
        }
        if (dataLoader != null) {
            ResourceRequestKey key = new ResourceRequestKey(uri, type, authorization);
            Mono<T> mono = Mono.fromFuture(dataLoader.load(key)).cast(type);
            if (manualDispatch) {
                dataLoader.dispatch();
            } else {
                dispatchIfQueued(dataLoader);
            }
            return mono;
        }
        return getDirect(uri, type, context, null);
    }

    public <T> Mono<T> get(String uri, Class<T> type, GraphQLServletContext context) {
        return getDirect(uri, type, context, null);
    }

    public <T> Mono<T> getDirect(String uri, Class<T> type, GraphQLServletContext context, String authorization) {
        Long queryId = GraphQLRequestAttributes.getQueryId(context);
        if (queryId == null) {
            queryId = queryIdProvider.nextId();
        }
        long requestSequence = GraphQLRequestAttributes.nextRequestSequence(context);
        if (requestSequence < 1) {
            requestSequence = fallbackRequestCounter.incrementAndGet();
        }
        String queryIdValue = formatQueryId(queryId);
        String requestIdValue = formatRequestId(requestSequence);
        long startNanos = System.nanoTime();

        String token = getToken(context);
        if (token == null) {
            token = authorization;
        }
        if (StringUtils.isBlank(token)) {
            throw new MissingAuthorizationException("Missing Authorization token");
        }

        log.debug("WebClient request start queryId={} requestId={} uri={} remote-IP={}", queryIdValue, requestIdValue, uri, getRemoteIp(context));

        final WebClient.RequestHeadersSpec<?> request = webClient.get().uri(uri);
        request.header(HttpHeaders.AUTHORIZATION, token);
        if (StringUtils.containsIgnoreCase(uri, "/kodeverk/")) {
            HashCode key = hashFunction.newHasher().putUnencodedChars(token).putUnencodedChars(uri).hash();
            final T result = (T) cache.getIfPresent(key);
            if (result != null) {
                log.trace("Cache hit on {}", uri);
                return Mono.just(result);
            }
            log.trace("Cache miss on {}", uri);
            return get(request, type, queryIdValue, requestIdValue, uri)
                    .doOnNext(value -> cache.put(key, value))
                    .doFinally(signal -> logRequestEnd(queryIdValue, requestIdValue, uri, startNanos));
        }
        return get(request, type, queryIdValue, requestIdValue, uri)
                .doFinally(signal -> logRequestEnd(queryIdValue, requestIdValue, uri, startNanos));
    }

    private <T> Mono<T> get(WebClient.RequestHeadersSpec<?> request, Class<T> type,
                            String queryIdValue, String requestIdValue, String uri) {
        return withPermit(
                request.retrieve()
                        .bodyToMono(type)
                        .onErrorResume(WebClientResponseException.class, ex -> {
                            log.error("WebClient response error: Status Code {}, URI {}, queryId={}, requestId={}, Message {}",
                                    ex.getRawStatusCode(), ex.getRequest().getURI(), queryIdValue, requestIdValue, ex.getMessage());
                            return Mono.error(ex);
                        })
                        .onErrorMap(ex -> {
                            if (ex instanceof WebClientResponseException) {
                                return ex;
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
                        }),
                queryIdValue,
                requestIdValue
        );
    }

    private <T> Mono<T> withPermit(Mono<T> mono, String queryIdValue, String requestIdValue) {
        return Mono.usingWhen(
                Mono.fromCallable(() -> {
                            try {
                                if (!concurrencyLimiter.tryAcquire()) {
                                    long parkedAt = System.nanoTime();
                                    log.debug("WebClient request queued queryId={} requestId={}", queryIdValue, requestIdValue);
                                    concurrencyLimiter.acquire();
                                    long waitMs = (System.nanoTime() - parkedAt) / 1_000_000;
                                    log.debug("WebClient request resumed queryId={} requestId={} waitMs={}",
                                            queryIdValue, requestIdValue, waitMs);
                                }
                                return concurrencyLimiter;
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                throw new IllegalStateException("Interrupted while waiting for WebClient concurrency permit", e);
                            }
                        })
                        .subscribeOn(Schedulers.boundedElastic()),
                ignored -> mono,
                semaphore -> Mono.fromRunnable(semaphore::release),
                (semaphore, error) -> Mono.fromRunnable(semaphore::release),
                semaphore -> Mono.fromRunnable(semaphore::release)
        );
    }

    private String getToken(GraphQLServletContext context) {
        return context != null ? context.getHttpServletRequest().getHeader(HttpHeaders.AUTHORIZATION) : null;
    }

    private DataLoader<ResourceRequestKey, Object> getDataLoader(DataFetchingEnvironment dfe) {
        if (dfe == null) {
            return null;
        }
        try {
            return dfe.getDataLoader(ResourceDataLoader.NAME);
        } catch (Exception ex) {
            return null;
        }
    }

    private DataLoader<ResourceRequestKey, Object> getRequestScopedDataLoader(DataFetchingEnvironment dfe,
                                                                              GraphQLServletContext context) {
        if (dfe == null || dfe.getExecutionId() == null) {
            return null;
        }
        return requestScopedLoaders.get(dfe.getExecutionId().toString(),
                key -> ResourceDataLoader.newDataLoader(this, context));
    }

    private void dispatchIfQueued(DataLoader<ResourceRequestKey, Object> dataLoader) {
        if (dataLoader.dispatchDepth() > 0) {
            dataLoader.dispatch();
        }
    }

    private void logRequestEnd(String queryIdValue, String requestIdValue, String uri, long startNanos) {
        long durationMs = (System.nanoTime() - startNanos) / 1_000_000;
        log.debug("WebClient request end queryId={} requestId={} durationMs={} uri={}",
                queryIdValue, requestIdValue, durationMs, uri);
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

    private String formatQueryId(Long queryId) {
        return queryId != null ? queryId.toString() : "unknown";
    }

    private String formatRequestId(long requestSequence) {
        return requestSequence > 0 ? Long.toString(requestSequence) : "unknown";
    }

    private void logRemoteIp(GraphQLServletContext context) {
        String ip = getRemoteIp(context);

        if (remoteAddresses.add(ip)) {
            log.debug("Remote IP: {}", ip);
        }
    }

    private GraphQLServletContext getContext(DataFetchingEnvironment dataFetchingEnvironment) {
        Object context = dataFetchingEnvironment.getContext();
        return context instanceof GraphQLServletContext ? (GraphQLServletContext) context : null;
    }

    private String getRemoteIp(GraphQLServletContext context) {
        if (context == null) return null;
        if (context.getHttpServletRequest() == null) return null;
        if (context.getHttpServletRequest().getRemoteAddr() == null) return null;
        return context.getHttpServletRequest().getRemoteAddr();
    }
}
