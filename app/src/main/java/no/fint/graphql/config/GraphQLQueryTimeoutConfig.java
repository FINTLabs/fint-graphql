package no.fint.graphql.config;

import graphql.ExecutionResultImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.tomcat.servlet.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
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
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> graphqlAsyncTimeoutCustomizer(
            @Value("${fint.graphql.async-request-timeout:PT1M55S}") Duration asyncTimeout) {
        // Spring GraphQL's MVC async response can bypass spring.mvc.async.request-timeout.
        // Set the container deadline explicitly to avoid Tomcat's 30-second default.
        long timeoutMs = Math.max(0, asyncTimeout.toMillis());
        return factory -> factory.addConnectorCustomizers(connector -> connector.setAsyncTimeout(timeoutMs));
    }

    @Bean
    public WebGraphQlInterceptor queryTimeoutInterceptor(
            @Value("${fint.graphql.query-timeout:PT1M50S}") Duration queryTimeout,
            @Value("${fint.graphql.async-request-timeout:PT1M55S}") Duration asyncTimeout) {
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
        if (asyncMs <= 0) {
            return queryMs;
        }
        // Leave time to serialize the GraphQL timeout response before the servlet expires.
        long latestQueryMs = Math.max(1, asyncMs - Math.min(1000, Math.max(1, asyncMs / 10)));
        return queryMs <= 0 ? latestQueryMs : Math.min(queryMs, latestQueryMs);
    }
}
