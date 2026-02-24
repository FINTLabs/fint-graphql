
package no.fint.graphql.model.model.fylke;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.kommune.KommuneService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.felles.kodeverk.FylkeResource;
import no.novari.fint.model.resource.felles.kodeverk.KommuneResource;
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

@Component("modelFylkeResolver")
public class FylkeResolver implements GraphQLResolver<FylkeResource> {

    @Autowired
    private KommuneService kommuneService;


    public CompletionStage<List<KommuneResource>> getKommune(FylkeResource fylke, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(fylke.getKommune()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> kommuneService.getKommuneResource(href, dfe)
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

