
package no.fint.graphql.model.model.elevfravar;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.aktivitetsfravar.AktivitetsfravarService;
import no.fint.graphql.model.model.elevforhold.ElevforholdService;
import no.fint.graphql.model.model.fravarsregistrering.FravarsregistreringService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.novari.fint.model.resource.utdanning.vurdering.AktivitetsfravarResource;
import no.novari.fint.model.resource.utdanning.vurdering.ElevfravarResource;
import no.novari.fint.model.resource.utdanning.vurdering.FravarsregistreringResource;
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

@Controller("modelElevfravarResolver")
public class ElevfravarResolver {

    @Autowired
    private AktivitetsfravarService aktivitetsfravarService;

    @Autowired
    private FravarsregistreringService fravarsregistreringService;

    @Autowired
    private ElevforholdService elevforholdService;


    @SchemaMapping(typeName = "Elevfravar", field = "aktivitetsfravar")
    public CompletionStage<List<AktivitetsfravarResource>> getAktivitetsfravar(ElevfravarResource elevfravar, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(elevfravar.getAktivitetsfravar()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> aktivitetsfravarService.getAktivitetsfravarResource(href, dfe)
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

    @SchemaMapping(typeName = "Elevfravar", field = "fravarsregistrering")
    public CompletionStage<List<FravarsregistreringResource>> getFravarsregistrering(ElevfravarResource elevfravar, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(elevfravar.getFravarsregistrering()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> fravarsregistreringService.getFravarsregistreringResource(href, dfe)
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

    @SchemaMapping(typeName = "Elevfravar", field = "elevforhold")
    public CompletionStage<ElevforholdResource> getElevforhold(ElevfravarResource elevfravar, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevfravar.getElevforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> elevforholdService.getElevforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

}

