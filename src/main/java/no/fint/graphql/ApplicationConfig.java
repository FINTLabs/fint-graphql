package no.fint.graphql;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ApplicationConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.create("https://play-with-fint.felleskomponent.no");
    }
}
