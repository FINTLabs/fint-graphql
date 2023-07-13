package no.fint.graphql;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import graphql.schema.DataFetchingEnvironment;
import graphql.servlet.context.GraphQLServletContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class WebClientRequest {

    private final WebClient webClient;
    private final Cache<HashCode, Object> cache;
    private final HashFunction hashFunction;
    private final BlacklistService blacklistService;

    public WebClientRequest(
            WebClient webClient,
            @Value("${fint.webclient.cache-spec:maximumSize=10000,expireAfterWrite=10m}") String cacheSpec,
            BlacklistService blacklistService) {
        this.webClient = webClient;
        cache = Caffeine.from(cacheSpec).build();
        this.blacklistService = blacklistService;
        hashFunction = Hashing.murmur3_128();
    }

    public <T> Mono<T> get(String uri, Class<T> type, DataFetchingEnvironment dfe) {
        GraphQLServletContext context = getContext(dfe);
        String token = getToken(context);
        logRemoteIp(context);
        blacklistService.failIfBlacklisted(getRemoteIp(context));

        final WebClient.RequestHeadersSpec<?> request = webClient.get().uri(uri);
        if (token != null) {
            request.header(HttpHeaders.AUTHORIZATION, token);
        }
        if (StringUtils.containsIgnoreCase(uri, "/kodeverk/")) {
            HashCode key = hashFunction.newHasher().putUnencodedChars(token).putUnencodedChars(uri).hash();
            final T result = (T) cache.getIfPresent(key);
            if (result != null) {
                log.trace("Cache hit on {}", uri);
                return Mono.just(result);
            }
            log.trace("Cache miss on {}", uri);
            return get(request, type)
                    .doOnNext(value -> cache.put(key, value));
        }
        return get(request, type);
    }

    private <T> Mono<T> get(WebClient.RequestHeadersSpec<?> request, Class<T> type) {
        return request.retrieve()
                .bodyToMono(type)
                .onErrorResume(WebClientResponseException.class,
                        ex -> ex.getRawStatusCode() == 404 ? Mono.empty() : Mono.error(ex))
                .retryBackoff(5, Duration.ofMillis(500));
    }

    private String getToken(GraphQLServletContext context) {
        return context != null ? context.getHttpServletRequest().getHeader(HttpHeaders.AUTHORIZATION) : null;
    }

    private Set<String> remoteAddresses = new HashSet<>();

    private void logRemoteIp(GraphQLServletContext context) {
        String ip = getRemoteIp(context);

        if (remoteAddresses.add(ip)) {
            log.info("Request-id: " + ip);
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
