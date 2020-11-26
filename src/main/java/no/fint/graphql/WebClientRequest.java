package no.fint.graphql;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import graphql.schema.DataFetchingEnvironment;
import graphql.servlet.context.GraphQLServletContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.cache.CacheMono;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Component
public class WebClientRequest {

    private final WebClient webClient;
    private final Cache<HashCode, Object> cache;
    private final HashFunction hashFunction;

    public WebClientRequest(
            WebClient webClient,
            @Value("${fint.webclient.cache-spec:maximumSize=10000,expireAfterWrite=10m}") String cacheSpec) {
        this.webClient = webClient;
        cache = Caffeine.from(cacheSpec).build();
        hashFunction = Hashing.murmur3_128();
    }

    public <T> Mono<T> get(String uri, Class<T> type, DataFetchingEnvironment dfe) {
        String token = getToken(dfe);
        log.debug("Token: {}", token);
        final WebClient.RequestHeadersSpec<?> request = webClient.get().uri(uri);
        if (token != null) {
            request.header(HttpHeaders.AUTHORIZATION, token);
        }
        if (StringUtils.containsIgnoreCase(uri, "/kodeverk/")) {
            HashCode key = hashFunction.newHasher().putString(token, Charsets.UTF_8).putString(uri, Charsets.UTF_8).hash();
            return CacheMono
                    .lookup(cache.asMap(), key, type)
                    .onCacheMissResume(() -> get(request, type));
        }
        return get(request, type);
    }

    private <T> Mono<T> get(WebClient.RequestHeadersSpec<?> request, Class<T> type) {
        return request.retrieve()
                .onStatus(HttpStatus::is4xxClientError, r -> Mono.empty())
                .bodyToMono(type)
                .retryBackoff(5, Duration.ofMillis(500));
    }

    private String getToken(DataFetchingEnvironment dfe) {
        Object context = dfe.getContext();
        if (context instanceof GraphQLServletContext) {
            return ((GraphQLServletContext) context).getHttpServletRequest().getHeader(HttpHeaders.AUTHORIZATION);
        }

        return null;
    }
}
