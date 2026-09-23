
package no.fint.graphql.model.model.karakterskala;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.karakterverdi.KarakterverdiService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.utdanning.kodeverk.KarakterskalaResource;
import no.novari.fint.model.resource.utdanning.vurdering.KarakterverdiResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Controller("modelKarakterskalaResolver")
public class KarakterskalaResolver {

    @Autowired
    private KarakterverdiService karakterverdiService;


    @SchemaMapping(typeName = "Karakterskala", field = "verdi")
    public CompletionStage<List<KarakterverdiResource>> getVerdi(KarakterskalaResource karakterskala, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(karakterskala.getVerdi()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> karakterverdiService.getKarakterverdiResource(href, dfe)
                        .map(Optional::of)
                        .defaultIfEmpty(Optional.empty())
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

