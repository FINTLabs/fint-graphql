package no.fint.graphql.config;

import graphql.ExecutionResult;
import graphql.ExecutionResultImpl;
import graphql.execution.instrumentation.ChainedInstrumentation;
import graphql.execution.instrumentation.Instrumentation;
import graphql.execution.instrumentation.SimpleInstrumentation;
import graphql.execution.instrumentation.dataloader.DataLoaderDispatcherInstrumentationOptions;
import graphql.execution.preparsed.NoOpPreparsedDocumentProvider;
import graphql.execution.preparsed.PreparsedDocumentProvider;
import graphql.kickstart.execution.BatchedDataLoaderGraphQLBuilder;
import graphql.kickstart.execution.GraphQLInvoker;
import graphql.kickstart.execution.GraphQLInvokerProxy;
import graphql.kickstart.execution.GraphQLQueryInvoker;
import graphql.kickstart.execution.config.ExecutionStrategyProvider;
import graphql.kickstart.execution.config.GraphQLBuilder;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Configuration
public class GraphQLQueryTimeoutConfig {

    @Value("${fint.graphql.query-timeout:PT1M50S}")
    private Duration timeout;

    @Bean
    @Primary
    public GraphQLQueryInvoker graphQLQueryInvoker(
            ExecutionStrategyProvider executionStrategyProvider,
            ObjectProvider<List<Instrumentation>> instrumentationProvider,
            ObjectProvider<PreparsedDocumentProvider> preparsedDocumentProvider,
            ObjectProvider<DataLoaderDispatcherInstrumentationOptions> dataLoaderOptionsProvider
    ) {
        List<Instrumentation> instrumentations = new ArrayList<>(
                Optional.ofNullable(instrumentationProvider.getIfAvailable()).orElse(Collections.emptyList())
        );
        if (!instrumentations.isEmpty()) {
            AnnotationAwareOrderComparator.sort(instrumentations);
        }
        Supplier<Instrumentation> instrumentationSupplier = () -> toInstrumentation(instrumentations);
        Supplier<PreparsedDocumentProvider> preparsedSupplier = () -> Optional
                .ofNullable(preparsedDocumentProvider.getIfAvailable())
                .orElse(NoOpPreparsedDocumentProvider.INSTANCE);
        Supplier<DataLoaderDispatcherInstrumentationOptions> dataLoaderOptionsSupplier = () -> Optional
                .ofNullable(dataLoaderOptionsProvider.getIfAvailable())
                .orElseGet(DataLoaderDispatcherInstrumentationOptions::newOptions);

        return new TimeoutGraphQLQueryInvoker(
                () -> executionStrategyProvider,
                instrumentationSupplier,
                preparsedSupplier,
                dataLoaderOptionsSupplier,
                timeout
        );
    }

    private static Instrumentation toInstrumentation(List<Instrumentation> instrumentations) {
        if (instrumentations == null || instrumentations.isEmpty()) {
            return SimpleInstrumentation.INSTANCE;
        }
        if (instrumentations.size() == 1) {
            return instrumentations.get(0);
        }
        return new ChainedInstrumentation(instrumentations);
    }

    private static final class TimeoutGraphQLQueryInvoker extends GraphQLQueryInvoker {
        private final Supplier<ExecutionStrategyProvider> executionStrategyProvider;
        private final Supplier<Instrumentation> instrumentation;
        private final Supplier<PreparsedDocumentProvider> preparsedDocumentProvider;
        private final Supplier<DataLoaderDispatcherInstrumentationOptions> dataLoaderOptions;
        private final long timeoutMillis;
        private final ExecutionResult timeoutResult;

        private TimeoutGraphQLQueryInvoker(
                Supplier<ExecutionStrategyProvider> executionStrategyProvider,
                Supplier<Instrumentation> instrumentation,
                Supplier<PreparsedDocumentProvider> preparsedDocumentProvider,
                Supplier<DataLoaderDispatcherInstrumentationOptions> dataLoaderOptions,
                Duration timeout
        ) {
            super(executionStrategyProvider, instrumentation, preparsedDocumentProvider, dataLoaderOptions);
            this.executionStrategyProvider = executionStrategyProvider;
            this.instrumentation = instrumentation;
            this.preparsedDocumentProvider = preparsedDocumentProvider;
            this.dataLoaderOptions = dataLoaderOptions;
            this.timeoutMillis = timeout != null ? timeout.toMillis() : 0L;
            this.timeoutResult = new ExecutionResultImpl(
                    null,
                    Collections.singletonList(new QueryTimeoutGraphQLError(this.timeoutMillis))
            );
        }

        @Override
        public GraphQLInvoker toGraphQLInvoker() {
            GraphQLBuilder builder = new GraphQLBuilder()
                    .executionStrategyProvider(executionStrategyProvider)
                    .instrumentation(instrumentation)
                    .preparsedDocumentProvider(preparsedDocumentProvider);
            GraphQLInvokerProxy proxy = (graphQL, executionInput) -> {
                CompletableFuture<ExecutionResult> result = graphQL.executeAsync(executionInput);
                if (timeoutMillis <= 0L) {
                    return result;
                }
                return result.completeOnTimeout(timeoutResult, timeoutMillis, TimeUnit.MILLISECONDS);
            };
            return new GraphQLInvoker(builder, new BatchedDataLoaderGraphQLBuilder(dataLoaderOptions), proxy);
        }
    }

    // QueryTimeoutGraphQLError is shared in config package for servlet async timeout handling.
}
