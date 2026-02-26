package no.fint.graphql.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.client.reactive.ReactorResourceFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

    @Value("${fint.endpoint.root:https://play-with-fint.felleskomponent.no}")
    private String rootUrl;

    @Value("${fint.webclient.connect-timeout-ms:20000}")
    private int connectTimeoutMs;

    @Value("${fint.webclient.response-timeout:PT30S}")
    private Duration responseTimeout;

    @Bean
    public WebClient webClient(WebClient.Builder builder, ReactorResourceFactory factory, ConnectionProvider connectionProvider) {
        factory.setConnectionProvider(connectionProvider);
        return builder
                .clientConnector(new ReactorClientHttpConnector(factory, httpClient -> {
                    HttpClient client = httpClient.secure();
                    return client.tcpConfiguration(tcp -> {
                        if (connectTimeoutMs > 0) {
                            tcp = tcp.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeoutMs);
                        }
                        if (responseTimeout != null && !responseTimeout.isZero() && !responseTimeout.isNegative()) {
                            long timeoutMillis = responseTimeout.toMillis();
                            if (timeoutMillis > 0) {
                                tcp = tcp.doOnConnected(conn -> {
                                    conn.addHandlerLast(new ReadTimeoutHandler(timeoutMillis, TimeUnit.MILLISECONDS));
                                    conn.addHandlerLast(new WriteTimeoutHandler(timeoutMillis, TimeUnit.MILLISECONDS));
                                });
                            }
                        }
                        return tcp;
                    });
                }))
                .baseUrl(rootUrl)
                .build();
    }

}
