package no.fint.graphql;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.client.reactive.ReactorResourceFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

@Configuration
public class ApplicationConfig {

    @Value("${fint.endpoint.root:https://play-with-fint.felleskomponent.no}")
    private String rootUri;

    /**
     * Build a WebClient using the Spring-supplied builder and ReactorResourceFactory.
     * Overrides the ConnectionProvider using a fixed Connection pool.
     * This pool can be configured with the properties {@link reactor.netty.ReactorNetty#POOL_ACQUIRE_TIMEOUT}
     * and {@link reactor.netty.ReactorNetty#POOL_MAX_CONNECTIONS}.
     * @param builder WebClient.Builder provided by Spring
     * @param factory ReactorResourceFactory provided by Spring
     * @return configured WebClient.
     */
    @Bean
    public WebClient webClient(WebClient.Builder builder, ReactorResourceFactory factory) {
        factory.setConnectionProvider(ConnectionProvider.fixed("fint"));
        return builder
                .clientConnector(new ReactorClientHttpConnector(factory, HttpClient::secure))
                .baseUrl(rootUri)
                .build();
    }
}
