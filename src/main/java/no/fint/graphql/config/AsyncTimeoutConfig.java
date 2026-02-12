package no.fint.graphql.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;

@Configuration
public class AsyncTimeoutConfig implements WebMvcConfigurer {

    @Value("${fint.endpoint.timeout:PT1M50S}")
    private Duration timeout;

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.setDefaultTimeout(timeout.toMillis());
    }
}
