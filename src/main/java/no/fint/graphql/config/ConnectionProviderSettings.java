package no.fint.graphql.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

@ConfigurationProperties("fint.webclient.connection-provider")
@Component
@Data
public class ConnectionProviderSettings {
    private String type = "fixed";
    private int maxConnections = ConnectionProvider.DEFAULT_POOL_MAX_CONNECTIONS;
    private int acquireMaxCount = 1;
    private long acquireTimeout = ConnectionProvider.DEFAULT_POOL_ACQUIRE_TIMEOUT;
    private Duration maxIdleTime = Duration.ofSeconds(30);
    private Duration maxLifeTime = Duration.ofSeconds(90);

    public int getEffectiveAcquireMaxCount() {
        return acquireMaxCount < 0 ? acquireMaxCount : Math.max(1, acquireMaxCount);
    }
}
