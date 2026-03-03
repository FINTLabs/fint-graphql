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
                ConnectionProvider.Builder fixedBuilder = ConnectionProvider.builder("graphql")
                        .maxConnections(settings.getMaxConnections())
                        .pendingAcquireMaxCount(1000)
                        .pendingAcquireTimeout(Duration.ofMillis(settings.getAcquireTimeout()));
                if (settings.getMaxIdleTime() != null) {
                    fixedBuilder.maxIdleTime(settings.getMaxIdleTime());
                }
                if (settings.getMaxLifeTime() != null) {
                    fixedBuilder.maxLifeTime(settings.getMaxLifeTime());
                }
                return fixedBuilder.build();
            case "ELASTIC":
                ConnectionProvider.Builder elasticBuilder = ConnectionProvider.builder("graphql");
                if (settings.getMaxIdleTime() != null) {
                    elasticBuilder.maxIdleTime(settings.getMaxIdleTime());
                }
                if (settings.getMaxLifeTime() != null) {
                    elasticBuilder.maxLifeTime(settings.getMaxLifeTime());
                }
                return elasticBuilder.build();
            case "NEW":
                return ConnectionProvider.newConnection();
            default:
                throw new IllegalArgumentException("Illegal connection provider type: " + settings.getType());
        }
    }
}
