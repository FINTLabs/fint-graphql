
package no.fint.graphql.model.model.rolle;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.fullmakt.FullmaktService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.administrasjon.fullmakt.FullmaktResource;
import no.novari.fint.model.resource.administrasjon.fullmakt.RolleResource;
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

@Controller("modelRolleResolver")
public class RolleResolver {

    @Autowired
    private FullmaktService fullmaktService;


    @SchemaMapping(typeName = "Rolle", field = "fullmakt")
    public CompletionStage<List<FullmaktResource>> getFullmakt(RolleResource rolle, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(rolle.getFullmakt()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> fullmaktService.getFullmaktResource(href, dfe)
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

