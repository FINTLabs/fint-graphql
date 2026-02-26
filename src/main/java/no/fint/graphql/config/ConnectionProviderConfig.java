package no.fint.graphql.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.resources.ConnectionProvider;
import java.time.Duration;

@Configuration
@Slf4j
public class ConnectionProviderConfig {

    @Bean
    public ConnectionProvider connectionProvider(ConnectionProviderSettings settings) {
        log.info("Connection Provider settings: {}", settings);
        switch (StringUtils.upperCase(settings.getType())) {
            case "FIXED":
                return ConnectionProvider.builder("graphql")
                        .maxConnections(settings.getMaxConnections())
                        .pendingAcquireMaxCount(1000)
                        .pendingAcquireTimeout(Duration.ofMillis(settings.getAcquireTimeout()))
                        .maxIdleTime(settings.getMaxIdleTime())
                        .maxLifeTime(settings.getMaxLifeTime())
                        .build();
            case "ELASTIC":
                return ConnectionProvider.builder("graphql")
                        .maxIdleTime(settings.getMaxIdleTime())
                        .maxLifeTime(settings.getMaxLifeTime())
                        .build();
            case "NEW":
                return ConnectionProvider.newConnection();
            default:
                throw new IllegalArgumentException("Illegal connection provider type: " + settings.getType());
        }
    }
}
