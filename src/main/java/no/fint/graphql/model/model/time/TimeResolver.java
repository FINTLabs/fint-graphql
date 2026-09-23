
package no.fint.graphql.model.model.time;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.rom.RomService;
import no.fint.graphql.model.model.undervisningsforhold.UndervisningsforholdService;
import no.fint.graphql.model.model.undervisningsgruppe.UndervisningsgruppeService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.utdanning.elev.UndervisningsforholdResource;
import no.novari.fint.model.resource.utdanning.timeplan.RomResource;
import no.novari.fint.model.resource.utdanning.timeplan.TimeResource;
import no.novari.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource;
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

@Controller("modelTimeResolver")
public class TimeResolver {

    @Autowired
    private UndervisningsgruppeService undervisningsgruppeService;

    @Autowired
    private UndervisningsforholdService undervisningsforholdService;

    @Autowired
    private RomService romService;


    @SchemaMapping(typeName = "Time", field = "undervisningsgruppe")
    public CompletionStage<List<UndervisningsgruppeResource>> getUndervisningsgruppe(TimeResource time, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(time.getUndervisningsgruppe()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> undervisningsgruppeService.getUndervisningsgruppeResource(href, dfe)
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

    @SchemaMapping(typeName = "Time", field = "undervisningsforhold")
    public CompletionStage<List<UndervisningsforholdResource>> getUndervisningsforhold(TimeResource time, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(time.getUndervisningsforhold()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> undervisningsforholdService.getUndervisningsforholdResource(href, dfe)
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

    @SchemaMapping(typeName = "Time", field = "rom")
    public CompletionStage<List<RomResource>> getRom(TimeResource time, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(time.getRom()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> romService.getRomResource(href, dfe)
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

