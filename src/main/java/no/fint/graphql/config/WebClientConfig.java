package no.fint.graphql.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ReactorResourceFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import io.netty.handler.logging.LogLevel;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

    @Value("${fint.endpoint.root:http://traefik.traefik-v2}")
    private String rootUrl;

    @Value("${fint.endpoint.host:beta.felleskomponent.no}")
    private String host;

    @Value("${fint.webclient.connect-timeout-ms:20000}")
    private int connectTimeoutMs;

    @Value("${fint.webclient.response-timeout:PT30S}")
    private Duration responseTimeout;

    @Bean
    public WebClient webClient(WebClient.Builder builder, ReactorResourceFactory factory, ConnectionProvider connectionProvider) {
        factory.setConnectionProvider(connectionProvider);
        return builder
                .clientConnector(new ReactorClientHttpConnector(factory, httpClient -> {
                    HttpClient client = httpClient.secure()
                            .wiretap("no.fint.graphql.downstream.netty", LogLevel.TRACE, AdvancedByteBufFormat.TEXTUAL);
                    if (connectTimeoutMs > 0) {
                        client = client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeoutMs);
                    }
                    if (responseTimeout != null && !responseTimeout.isZero() && !responseTimeout.isNegative()) {
                        long timeoutMillis = responseTimeout.toMillis();
                        if (timeoutMillis > 0) {
                            client = client
                                    .responseTimeout(Duration.ofMillis(timeoutMillis))
                                    .doOnConnected(conn -> {
                                        conn.addHandlerLast(new ReadTimeoutHandler(timeoutMillis, TimeUnit.MILLISECONDS));
                                        conn.addHandlerLast(new WriteTimeoutHandler(timeoutMillis, TimeUnit.MILLISECONDS));
                                    });
                        }
                    }
                    return client;
                }))
                .baseUrl(rootUrl)
                .defaultHeader("Host", host)
                .build();
    }

    @Bean
    public ReactorResourceFactory reactorResourceFactory() {
        ReactorResourceFactory factory = new ReactorResourceFactory();
        factory.setUseGlobalResources(false);
        return factory;
    }
}
