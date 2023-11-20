package no.fint.graphql.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.client.reactive.ReactorResourceFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;
import java.util.List;

@Configuration
@Slf4j
public class ApplicationConfig {

    @Getter
    @Value("${fint.graphql.blacklist:}")
    private List<String> blacklist;

    @Bean
    public ConnectionProvider connectionProvider(ConnectionProviderSettings settings) {
        log.info("Connection Provider settings: {}", settings);
        return switch (StringUtils.upperCase(settings.getType())) {
            case "FIXED" -> ConnectionProvider.builder("graphql")
                    .maxConnections(settings.getMaxConnections())
                    .pendingAcquireTimeout(Duration.ofMillis(settings.getAcquireTimeout()))
                    .maxIdleTime(settings.getMaxIdleTime())
                    .maxLifeTime(settings.getMaxLifeTime())
                    .build();
            case "ELASTIC" -> ConnectionProvider.builder("graphql")
                    .maxIdleTime(settings.getMaxIdleTime())
                    .maxLifeTime(settings.getMaxLifeTime())
                    .build();
            case "NEW" -> ConnectionProvider.newConnection();
            default -> throw new IllegalArgumentException("Illegal connection provider type: " + settings.getType());
        };
    }
}
