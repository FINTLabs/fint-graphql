package no.fint.graphql.config;

import graphql.ExecutionResultImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.graphql.support.DefaultExecutionGraphQlResponse;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Configuration
public class GraphQLQueryTimeoutConfig {
    @Bean
    public WebGraphQlInterceptor queryTimeoutInterceptor(
            @Value("${fint.graphql.query-timeout:PT1M50S}") Duration queryTimeout,
            @Value("${fint.graphql.async-request-timeout:PT2M}") Duration asyncTimeout) {
        long timeoutMs = effectiveTimeoutMillis(queryTimeout, asyncTimeout);
        return (request, chain) -> {
            Mono<WebGraphQlResponse> result = chain.next(request);
            if (timeoutMs <= 0) {
                return result;
            }
            return result.timeout(Duration.ofMillis(timeoutMs), Mono.fromSupplier(() ->
                    new WebGraphQlResponse(new DefaultExecutionGraphQlResponse(request.toExecutionInput(),
                            new ExecutionResultImpl(null, List.of(new QueryTimeoutGraphQLError(timeoutMs)))))));
        };
    }

    static long effectiveTimeoutMillis(Duration queryTimeout, Duration asyncTimeout) {
        long queryMs = queryTimeout.toMillis();
        long asyncMs = asyncTimeout.toMillis();
        return asyncMs > 0 && (queryMs <= 0 || asyncMs < queryMs) ? asyncMs : queryMs;
    }
}
