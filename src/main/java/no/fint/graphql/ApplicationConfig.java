package no.fint.graphql;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.client.reactive.ReactorResourceFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

@Configuration
@Slf4j
public class ApplicationConfig {

    @Value("${fint.endpoint.root:https://play-with-fint.felleskomponent.no}")
    private String rootUri;

    @Bean
    public WebClient webClient(WebClient.Builder builder, ReactorResourceFactory factory) {
        factory.setConnectionProvider(ConnectionProvider.newConnection());
        return builder
                .clientConnector(new ReactorClientHttpConnector(factory, HttpClient::secure))
                .baseUrl(rootUri)
                .build();
    }

    @PostConstruct
    public void init() throws IOException {
        Path path = Paths.get("build.properties");
        if (Files.exists(path)) {
            Properties properties = new Properties();
            properties.load(Files.newInputStream(path));
            properties.forEach((k, v) -> log.info("{}: {}", k, v));
        }
    }
}
