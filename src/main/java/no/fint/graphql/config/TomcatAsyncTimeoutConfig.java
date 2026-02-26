package no.fint.graphql.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class TomcatAsyncTimeoutConfig implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    @Value("${fint.graphql.async-request-timeout:PT2M}")
    private Duration timeout;

    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        long timeoutMs = timeout != null ? timeout.toMillis() : 0L;
        if (timeoutMs > 0L) {
            factory.addConnectorCustomizers(connector -> connector.setAsyncTimeout(timeoutMs));
        }
    }
}
