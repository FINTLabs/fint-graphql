
package no.fint.graphql.model.model.elevvurdering;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.eksamensvurdering.EksamensvurderingService;
import no.fint.graphql.model.model.elevforhold.ElevforholdService;
import no.fint.graphql.model.model.halvarsfagvurdering.HalvarsfagvurderingService;
import no.fint.graphql.model.model.halvarsordensvurdering.HalvarsordensvurderingService;
import no.fint.graphql.model.model.sluttfagvurdering.SluttfagvurderingService;
import no.fint.graphql.model.model.sluttordensvurdering.SluttordensvurderingService;
import no.fint.graphql.model.model.underveisfagvurdering.UnderveisfagvurderingService;
import no.fint.graphql.model.model.underveisordensvurdering.UnderveisordensvurderingService;
import no.fint.graphql.model.model.vitnemalsmerknad.VitnemalsmerknadService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.novari.fint.model.resource.utdanning.kodeverk.VitnemalsmerknadResource;
import no.novari.fint.model.resource.utdanning.vurdering.*;
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

@Component("modelElevvurderingResolver")
public class ElevvurderingResolver implements GraphQLResolver<ElevvurderingResource> {

    @Autowired
    private ElevforholdService elevforholdService;

    @Autowired
    private SluttfagvurderingService sluttfagvurderingService;

    @Autowired
    private UnderveisordensvurderingService underveisordensvurderingService;

    @Autowired
    private VitnemalsmerknadService vitnemalsmerknadService;

    @Autowired
    private UnderveisfagvurderingService underveisfagvurderingService;

    @Autowired
    private HalvarsordensvurderingService halvarsordensvurderingService;

    @Autowired
    private HalvarsfagvurderingService halvarsfagvurderingService;

    @Autowired
    private SluttordensvurderingService sluttordensvurderingService;

    @Autowired
    private EksamensvurderingService eksamensvurderingService;


    public CompletionStage<ElevforholdResource> getElevforhold(ElevvurderingResource elevvurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevvurdering.getElevforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> elevforholdService.getElevforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<List<SluttfagvurderingResource>> getSluttfagvurdering(ElevvurderingResource elevvurdering, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(elevvurdering.getSluttfagvurdering()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> sluttfagvurderingService.getSluttfagvurderingResource(href, dfe)
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

    public CompletionStage<List<UnderveisordensvurderingResource>> getUnderveisordensvurdering(ElevvurderingResource elevvurdering, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(elevvurdering.getUnderveisordensvurdering()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> underveisordensvurderingService.getUnderveisordensvurderingResource(href, dfe)
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

    public CompletionStage<List<VitnemalsmerknadResource>> getVitnemalsmerknad(ElevvurderingResource elevvurdering, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(elevvurdering.getVitnemalsmerknad()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> vitnemalsmerknadService.getVitnemalsmerknadResource(href, dfe)
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

    public CompletionStage<List<UnderveisfagvurderingResource>> getUnderveisfagvurdering(ElevvurderingResource elevvurdering, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(elevvurdering.getUnderveisfagvurdering()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> underveisfagvurderingService.getUnderveisfagvurderingResource(href, dfe)
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

    public CompletionStage<List<HalvarsordensvurderingResource>> getHalvarsordensvurdering(ElevvurderingResource elevvurdering, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(elevvurdering.getHalvarsordensvurdering()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> halvarsordensvurderingService.getHalvarsordensvurderingResource(href, dfe)
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

    public CompletionStage<List<HalvarsfagvurderingResource>> getHalvarsfagvurdering(ElevvurderingResource elevvurdering, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(elevvurdering.getHalvarsfagvurdering()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> halvarsfagvurderingService.getHalvarsfagvurderingResource(href, dfe)
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

    public CompletionStage<List<SluttordensvurderingResource>> getSluttordensvurdering(ElevvurderingResource elevvurdering, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(elevvurdering.getSluttordensvurdering()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> sluttordensvurderingService.getSluttordensvurderingResource(href, dfe)
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

    public CompletionStage<List<EksamensvurderingResource>> getEksamensvurdering(ElevvurderingResource elevvurdering, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(elevvurdering.getEksamensvurdering()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> eksamensvurderingService.getEksamensvurderingResource(href, dfe)
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

