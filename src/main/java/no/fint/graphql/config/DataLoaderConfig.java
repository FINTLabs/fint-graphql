package no.fint.graphql.config;

import graphql.kickstart.execution.context.GraphQLKickstartContext;
import graphql.kickstart.servlet.context.GraphQLServletContextBuilder;
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
    public GraphQLServletContextBuilder graphQLServletContextBuilder() {
        return new GraphQLServletContextBuilder() {
            @Override
            public GraphQLKickstartContext build(HttpServletRequest request, HttpServletResponse response) {
                return GraphQLKickstartContext.of(createContextMap(request, response));
            }

            @Override
            public GraphQLKickstartContext build(Session session, HandshakeRequest request) {
                return GraphQLKickstartContext.of(createContextMap(session, request));
            }

            @Override
            public GraphQLKickstartContext build() {
                return GraphQLKickstartContext.of(new HashMap<>());
            }
        };
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
