package no.fint.graphql.config;

import no.fint.graphql.WebClientGraphQLErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.server.WebGraphQlHandler;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.graphql.server.webmvc.GraphQlHttpHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@Configuration
public class GraphQLTransportConfig {
    @Bean
    public GraphQlHttpHandler graphQlHttpHandler(WebGraphQlHandler handler) {
        return new GraphQlHttpHandler(handler) {
            @Override
            protected HttpStatus selectResponseStatus(WebGraphQlResponse response, MediaType mediaType) {
                boolean unauthorized = response.getErrors().stream().anyMatch(error ->
                        Integer.valueOf(401).equals(error.getExtensions().get("code"))
                                && "Unauthorized".equals(error.getMessage()));
                return unauthorized ? HttpStatus.UNAUTHORIZED : super.selectResponseStatus(response, mediaType);
            }
        };
    }

    @Bean
    public WebGraphQlInterceptor resultErrorInterceptor(WebClientGraphQLErrorHandler errors) {
        return (request, chain) -> chain.next(request).map(response -> response.transform(builder ->
                builder.errors(errors.processErrors(response.getExecutionResult().getErrors()))));
    }
}
