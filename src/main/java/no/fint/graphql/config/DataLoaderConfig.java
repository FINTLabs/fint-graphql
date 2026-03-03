package no.fint.graphql.config;

import graphql.execution.instrumentation.Instrumentation;
import graphql.execution.instrumentation.dataloader.DataLoaderDispatcherInstrumentation;
import graphql.kickstart.execution.context.GraphQLKickstartContext;
import graphql.kickstart.servlet.context.GraphQLServletContextBuilder;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.dataloader.ResourceDataLoader;
import org.dataloader.DataLoaderRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.websocket.Session;
import jakarta.websocket.server.HandshakeRequest;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataLoaderConfig {

    @Bean
    public Instrumentation dataLoaderDispatcherInstrumentation() {
        return new DataLoaderDispatcherInstrumentation();
    }

    @Bean
    public GraphQLServletContextBuilder graphQLServletContextBuilder(WebClientRequest webClientRequest) {
        return new GraphQLServletContextBuilder() {
            @Override
            public GraphQLKickstartContext build(HttpServletRequest request, HttpServletResponse response) {
                return GraphQLKickstartContext.of(
                        buildRegistry(webClientRequest),
                        createContextMap(request, response)
                );
            }

            @Override
            public GraphQLKickstartContext build(Session session, HandshakeRequest request) {
                return GraphQLKickstartContext.of(
                        buildRegistry(webClientRequest),
                        createContextMap(session, request)
                );
            }

            @Override
            public GraphQLKickstartContext build() {
                return GraphQLKickstartContext.of(buildRegistry(webClientRequest));
            }
        };
    }

    private DataLoaderRegistry buildRegistry(WebClientRequest webClientRequest) {
        DataLoaderRegistry registry = new DataLoaderRegistry();
        registry.register(ResourceDataLoader.NAME, ResourceDataLoader.newDataLoader(webClientRequest));
        return registry;
    }

    private Map<Object, Object> createContextMap(HttpServletRequest request, HttpServletResponse response) {
        Map<Object, Object> contextMap = new HashMap<>();
        if (request != null) {
            contextMap.put(HttpServletRequest.class, request);
        }
        if (response != null) {
            contextMap.put(HttpServletResponse.class, response);
        }
        return contextMap;
    }

    private Map<Object, Object> createContextMap(Session session, HandshakeRequest request) {
        Map<Object, Object> contextMap = new HashMap<>();
        if (session != null) {
            contextMap.put(Session.class, session);
        }
        if (request != null) {
            contextMap.put(HandshakeRequest.class, request);
        }
        return contextMap;
    }
}
