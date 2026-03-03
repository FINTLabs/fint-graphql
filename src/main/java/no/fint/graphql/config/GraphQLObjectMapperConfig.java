package no.fint.graphql.config;

import graphql.kickstart.execution.GraphQLObjectMapper;
import graphql.kickstart.execution.config.DefaultExecutionStrategyProvider;
import graphql.kickstart.execution.config.ExecutionStrategyProvider;
import graphql.kickstart.execution.error.DefaultGraphQLErrorHandler;
import graphql.kickstart.execution.error.GraphQLErrorHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GraphQLObjectMapperConfig {

    @Bean
    public GraphQLObjectMapper graphQLObjectMapper(ObjectProvider<GraphQLErrorHandler> errorHandlerProvider) {
        GraphQLErrorHandler errorHandler = errorHandlerProvider.getIfAvailable(DefaultGraphQLErrorHandler::new);
        return GraphQLObjectMapper.newBuilder()
                .withGraphQLErrorHandler(() -> errorHandler)
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public ExecutionStrategyProvider executionStrategyProvider() {
        return new DefaultExecutionStrategyProvider();
    }
}
