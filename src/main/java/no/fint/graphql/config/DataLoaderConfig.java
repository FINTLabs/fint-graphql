package no.fint.graphql.config;

import graphql.execution.instrumentation.Instrumentation;
import graphql.execution.instrumentation.dataloader.DataLoaderDispatcherInstrumentation;
import graphql.kickstart.execution.context.GraphQLContext;
import graphql.servlet.context.DefaultGraphQLServletContext;
import graphql.servlet.context.GraphQLServletContext;
import graphql.servlet.context.GraphQLServletContextBuilder;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.dataloader.ResourceDataLoader;
import org.dataloader.DataLoaderRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;
import javax.websocket.server.HandshakeRequest;

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
            public GraphQLContext build(HttpServletRequest request, HttpServletResponse response) {
                DataLoaderRegistry registry = new DataLoaderRegistry();
                GraphQLServletContext servletContext = DefaultGraphQLServletContext.createServletContext(registry, null)
                        .with(request)
                        .with(response)
                        .build();
                registry.register(ResourceDataLoader.NAME, ResourceDataLoader.newDataLoader(webClientRequest, servletContext));
                return servletContext;
            }

            @Override
            public GraphQLContext build(Session session, HandshakeRequest request) {
                DataLoaderRegistry registry = new DataLoaderRegistry();
                GraphQLServletContext servletContext = DefaultGraphQLServletContext.createServletContext(registry, null)
                        .build();
                registry.register(ResourceDataLoader.NAME, ResourceDataLoader.newDataLoader(webClientRequest, servletContext));
                return servletContext;
            }

            @Override
            public GraphQLContext build() {
                DataLoaderRegistry registry = new DataLoaderRegistry();
                GraphQLServletContext servletContext = DefaultGraphQLServletContext.createServletContext(registry, null)
                        .build();
                registry.register(ResourceDataLoader.NAME, ResourceDataLoader.newDataLoader(webClientRequest, servletContext));
                return servletContext;
            }
        };
    }
}
