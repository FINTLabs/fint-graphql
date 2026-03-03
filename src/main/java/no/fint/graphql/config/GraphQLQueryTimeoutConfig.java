package no.fint.graphql.config;

import graphql.ExecutionResult;
import graphql.ExecutionResultImpl;
import graphql.kickstart.execution.BatchedDataLoaderGraphQLBuilder;
import graphql.kickstart.execution.GraphQLInvoker;
import graphql.kickstart.execution.GraphQLQueryResult;
import graphql.kickstart.execution.config.GraphQLBuilder;
import graphql.kickstart.execution.input.GraphQLInvocationInput;
import graphql.kickstart.execution.input.GraphQLSingleInvocationInput;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Configuration
public class GraphQLQueryTimeoutConfig {

    @Bean
    public GraphQLInvoker graphQLInvoker(
            GraphQLBuilder graphQLBuilder,
            BatchedDataLoaderGraphQLBuilder batchedDataLoaderGraphQLBuilder,
            @Value("${fint.graphql.query-timeout:PT1M50S}") Duration queryTimeout,
            @Value("${fint.graphql.async-request-timeout:PT2M}") Duration asyncRequestTimeout
    ) {
        return new TimeoutGraphQLInvoker(
                graphQLBuilder,
                batchedDataLoaderGraphQLBuilder,
                getEffectiveTimeoutMillis(queryTimeout, asyncRequestTimeout)
        );
    }

    private static long getEffectiveTimeoutMillis(Duration queryTimeout, Duration asyncRequestTimeout) {
        long queryTimeoutMillis = queryTimeout != null ? queryTimeout.toMillis() : 0L;
        long asyncTimeoutMillis = asyncRequestTimeout != null ? asyncRequestTimeout.toMillis() : 0L;
        long timeoutMillis = queryTimeoutMillis;
        if (asyncTimeoutMillis > 0L && (timeoutMillis <= 0L || asyncTimeoutMillis < timeoutMillis)) {
            timeoutMillis = asyncTimeoutMillis;
        }
        return timeoutMillis;
    }

    private static final class TimeoutGraphQLInvoker extends GraphQLInvoker {
        private final long timeoutMillis;
        private final ExecutionResult timeoutExecutionResult;
        private final GraphQLQueryResult timeoutQueryResult;

        private TimeoutGraphQLInvoker(
                GraphQLBuilder graphQLBuilder,
                BatchedDataLoaderGraphQLBuilder batchedDataLoaderGraphQLBuilder,
                long timeoutMillis
        ) {
            super(graphQLBuilder, batchedDataLoaderGraphQLBuilder);
            this.timeoutMillis = timeoutMillis;
            this.timeoutExecutionResult = new ExecutionResultImpl(
                    null,
                    Collections.singletonList(new QueryTimeoutGraphQLError(timeoutMillis))
            );
            this.timeoutQueryResult = GraphQLQueryResult.create(timeoutExecutionResult);
        }

        @Override
        public CompletableFuture<ExecutionResult> executeAsync(GraphQLSingleInvocationInput invocationInput) {
            CompletableFuture<ExecutionResult> future = super.executeAsync(invocationInput);
            if (timeoutMillis <= 0L) {
                return future;
            }
            return future.completeOnTimeout(timeoutExecutionResult, timeoutMillis, TimeUnit.MILLISECONDS);
        }

        @Override
        public GraphQLQueryResult query(GraphQLInvocationInput invocationInput) {
            if (timeoutMillis <= 0L) {
                return super.query(invocationInput);
            }
            return queryAsync(invocationInput).join();
        }

        @Override
        public CompletableFuture<GraphQLQueryResult> queryAsync(GraphQLInvocationInput invocationInput) {
            CompletableFuture<GraphQLQueryResult> future = super.queryAsync(invocationInput);
            if (timeoutMillis <= 0L) {
                return future;
            }
            return future.completeOnTimeout(timeoutQueryResult, timeoutMillis, TimeUnit.MILLISECONDS);
        }
    }
}
