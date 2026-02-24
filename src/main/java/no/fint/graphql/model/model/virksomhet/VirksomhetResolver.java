
package no.fint.graphql.model.model.virksomhet;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.larling.LarlingService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.felles.VirksomhetResource;
import no.novari.fint.model.resource.utdanning.larling.LarlingResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Component("modelVirksomhetResolver")
public class VirksomhetResolver implements GraphQLResolver<VirksomhetResource> {

    @Autowired
    private LarlingService larlingService;


    public CompletionStage<List<LarlingResource>> getLarling(VirksomhetResource virksomhet, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(virksomhet.getLarling()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> larlingService.getLarlingResource(href, dfe)
                        .map(Optional::of)
                        .onErrorResume(WebClientResponseException.class,
                                ex -> Mono.just(Optional.empty())),
                        8, 1)
                .collectList()
                .map(list -> list.stream()
                        .map(opt -> opt.orElse(null))
                        .collect(Collectors.toList()))
                .toFuture();
    }

}

