package no.fint.graphql.dataloader;

import graphql.kickstart.execution.context.GraphQLKickstartContext;
import no.fint.graphql.WebClientRequest;
import org.dataloader.BatchLoaderWithContext;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderFactory;
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
                                                                        GraphQLKickstartContext context) {
        BatchLoaderWithContext<ResourceRequestKey, Try<Object>> loader = (keys, env) -> {
            GraphQLKickstartContext graphQLContext = context != null
                    ? context
                    : extractContext(env.getContext());
            return Flux.fromIterable(keys)
                    .flatMapSequential(key -> {
                        @SuppressWarnings("unchecked")
                        Class<Object> type = (Class<Object>) key.getType();
                        return webClientRequest.getDirect(key.getUri(), type, graphQLContext, key.getAuthorization())
                                .cast(Object.class)
                                .map(Try::succeeded)
                                .onErrorResume(ex -> Mono.just(Try.failed(ex)));
                    })
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
        if (context != null) {
            options.setBatchLoaderContextProvider(() -> context);
        }
        return DataLoaderFactory.newDataLoaderWithTry(loader, options);
    }

    private static GraphQLKickstartContext extractContext(Object context) {
        return context instanceof GraphQLKickstartContext ? (GraphQLKickstartContext) context : null;
    }
}
