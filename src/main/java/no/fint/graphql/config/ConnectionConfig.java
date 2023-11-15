package no.fint.graphql.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.resources.ConnectionProvider;

@Configuration
@Slf4j
public class ConnectionConfig {

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
