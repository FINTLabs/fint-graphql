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

    @Value("${fint.webclient.pool.size:10}")
    private int poolSize;

    @Value("${fint.webclient.pool.timeout:1500}")
    private long poolTimeout;

    @Bean
    public WebClient webClient(WebClient.Builder builder, ReactorResourceFactory factory) {
        factory.setConnectionProvider(ConnectionProvider.fixed("fint", poolSize, poolTimeout));
        return builder
                .clientConnector(new ReactorClientHttpConnector(factory, HttpClient::secure))
                .baseUrl(rootUri)
                .build();
    }
}
