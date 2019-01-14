package no.fint.graphql;

import graphql.schema.DataFetchingEnvironment;
import graphql.servlet.GraphQLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.function.Function;

@Component
public class WebClientRequest {

    @Autowired
    private WebClient webClient;

    public <T> T get(Function<UriBuilder, URI> uri, Class<T> type, DataFetchingEnvironment dfe) {
        WebClient.RequestHeadersSpec<?> request = webClient.get().uri(uri);
        return get(request, type, dfe);
    }

    public <T> T get(String uri, Class<T> type, DataFetchingEnvironment dfe) {
        WebClient.RequestHeadersSpec<?> request = webClient.get().uri(uri);
        return get(request, type, dfe);
    }

    private <T> T get(WebClient.RequestHeadersSpec<?> request, Class<T> type, DataFetchingEnvironment dfe) {
        String token = getToken(dfe);
        if (token != null) {
            request.header(HttpHeaders.AUTHORIZATION, token);
        }
        return request.retrieve()
                .onStatus(HttpStatus::is4xxClientError, r -> Mono.empty())
                .bodyToMono(type)
                .block();
    }

    private String getToken(DataFetchingEnvironment dfe) {
        Object context = dfe.getExecutionContext().getContext();
        if (context instanceof GraphQLContext) {
            return ((GraphQLContext) context).getHttpServletRequest()
                    .map(r -> r.getHeader(HttpHeaders.AUTHORIZATION))
                    .orElse(null);
        }

        return null;
    }
}
