
package no.fint.graphql.model.model.prosjekt;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.prosjektart.ProsjektartService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.administrasjon.kodeverk.ProsjektResource;
import no.novari.fint.model.resource.administrasjon.kodeverk.ProsjektartResource;
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

@Component("modelProsjektResolver")
public class ProsjektResolver implements GraphQLResolver<ProsjektResource> {

    @Autowired
    private ProsjektartService prosjektartService;


    public CompletionStage<List<ProsjektartResource>> getProsjektart(ProsjektResource prosjekt, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(prosjekt.getProsjektart()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> prosjektartService.getProsjektartResource(href, dfe)
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

