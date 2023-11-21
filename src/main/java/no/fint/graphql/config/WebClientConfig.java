package no.fint.graphql.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.client.reactive.ReactorResourceFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

@Configuration
public class WebClientConfig {

    @Value("${fint.endpoint.root:https://play-with-fint.felleskomponent.no}")
    private String rootUrl;

    @Bean
    public WebClient webClient(WebClient.Builder builder, ReactorResourceFactory factory, ConnectionProvider connectionProvider) {
        factory.setConnectionProvider(connectionProvider);
        return builder
                .clientConnector(new ReactorClientHttpConnector(factory, HttpClient::secure))
                .baseUrl(rootUrl)
                .build();
    }

}
