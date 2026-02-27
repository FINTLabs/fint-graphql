package no.fint.graphql.dataloader;

import graphql.servlet.context.GraphQLServletContext;
import no.fint.graphql.WebClientRequest;
import org.dataloader.BatchLoaderWithContext;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderOptions;
import org.dataloader.Try;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public final class ResourceDataLoader {
    public static final String NAME = "resourceByUri";

    private ResourceDataLoader() {
    }

    public static DataLoader<ResourceRequestKey, Object> newDataLoader(WebClientRequest webClientRequest) {
        return newDataLoader(webClientRequest, null);
    }

    public static DataLoader<ResourceRequestKey, Object> newDataLoader(WebClientRequest webClientRequest,
                                                                       GraphQLServletContext servletContext) {
        BatchLoaderWithContext<ResourceRequestKey, Try<Object>> loader = (keys, env) -> {
            GraphQLServletContext context = servletContext != null
                    ? servletContext
                    : extractServletContext(env.getContext());
            int maxConcurrency = Math.max(1, webClientRequest.getMaxInFlightRequests());
            return Flux.fromIterable(keys)
                    .flatMapSequential(key -> {
                        @SuppressWarnings("unchecked")
                        Class<Object> type = (Class<Object>) key.getType();
                        return webClientRequest.getDirect(key.getUri(), type, context, key.getAuthorization())
                                .cast(Object.class)
                                .map(Try::succeeded)
                                .onErrorResume(ex -> Mono.just(Try.failed(ex)));
                    }, maxConcurrency, maxConcurrency)
                    .collectList()
                    .toFuture();
        };
        DataLoaderOptions options = DataLoaderOptions.newOptions()
                .setCachingEnabled(true)
                .setCacheKeyFunction(key -> {
                    ResourceRequestKey requestKey = (ResourceRequestKey) key;
                    return requestKey.getUri()
                            + "::"
                            + requestKey.getType().getName()
                            + "::"
                            + requestKey.getAuthorization();
                });
        if (servletContext != null) {
            options.setBatchLoaderContextProvider(() -> servletContext);
        }
        return DataLoader.newDataLoaderWithTry(loader, options);
    }

    private static GraphQLServletContext extractServletContext(Object context) {
        return context instanceof GraphQLServletContext ? (GraphQLServletContext) context : null;
    }
}
