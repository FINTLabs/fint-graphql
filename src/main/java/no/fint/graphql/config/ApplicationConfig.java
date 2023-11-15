package no.fint.graphql.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import no.fint.graphql.ConnectionProviderSettings;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.client.reactive.ReactorResourceFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

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
        switch (StringUtils.upperCase(settings.getType())) {
            case "FIXED":
                return ConnectionProvider.fixed("graphql", settings.getMaxConnections(), settings.getAcquireTimeout(), settings.getMaxIdleTime(), settings.getMaxLifeTime());
            case "ELASTIC":
                return ConnectionProvider.elastic("graphql", settings.getMaxIdleTime(), settings.getMaxLifeTime());
            case "NEW":
                return ConnectionProvider.newConnection();
            default:
                throw new IllegalArgumentException("Illegal connection provider type: " + settings.getType());
        }
    }
}