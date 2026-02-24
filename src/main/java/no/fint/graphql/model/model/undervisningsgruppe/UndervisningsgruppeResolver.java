
package no.fint.graphql.model.model.undervisningsgruppe;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.fag.FagService;
import no.fint.graphql.model.model.skole.SkoleService;
import no.fint.graphql.model.model.skolear.SkolearService;
import no.fint.graphql.model.model.termin.TerminService;
import no.fint.graphql.model.model.time.TimeService;
import no.fint.graphql.model.model.undervisningsforhold.UndervisningsforholdService;
import no.fint.graphql.model.model.undervisningsgruppemedlemskap.UndervisningsgruppemedlemskapService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.utdanning.elev.UndervisningsforholdResource;
import no.novari.fint.model.resource.utdanning.kodeverk.SkolearResource;
import no.novari.fint.model.resource.utdanning.kodeverk.TerminResource;
import no.novari.fint.model.resource.utdanning.timeplan.FagResource;
import no.novari.fint.model.resource.utdanning.timeplan.TimeResource;
import no.novari.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource;
import no.novari.fint.model.resource.utdanning.timeplan.UndervisningsgruppemedlemskapResource;
import no.novari.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
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

@Component("modelUndervisningsgruppeResolver")
public class UndervisningsgruppeResolver implements GraphQLResolver<UndervisningsgruppeResource> {

    @Autowired
    private UndervisningsforholdService undervisningsforholdService;

    @Autowired
    private FagService fagService;

    @Autowired
    private TerminService terminService;

    @Autowired
    private SkoleService skoleService;

    @Autowired
    private SkolearService skolearService;

    @Autowired
    private TimeService timeService;

    @Autowired
    private UndervisningsgruppemedlemskapService undervisningsgruppemedlemskapService;


    public CompletionStage<List<UndervisningsforholdResource>> getUndervisningsforhold(UndervisningsgruppeResource undervisningsgruppe, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(undervisningsgruppe.getUndervisningsforhold()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> undervisningsforholdService.getUndervisningsforholdResource(href, dfe)
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

    public CompletionStage<List<FagResource>> getFag(UndervisningsgruppeResource undervisningsgruppe, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(undervisningsgruppe.getFag()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> fagService.getFagResource(href, dfe)
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

    public CompletionStage<List<TerminResource>> getTermin(UndervisningsgruppeResource undervisningsgruppe, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(undervisningsgruppe.getTermin()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> terminService.getTerminResource(href, dfe)
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

    public CompletionStage<SkoleResource> getSkole(UndervisningsgruppeResource undervisningsgruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(undervisningsgruppe.getSkole()
                .stream()
                .map(Link::getHref)
                .map(l -> skoleService.getSkoleResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<SkolearResource> getSkolear(UndervisningsgruppeResource undervisningsgruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(undervisningsgruppe.getSkolear()
                .stream()
                .map(Link::getHref)
                .map(l -> skolearService.getSkolearResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<List<TimeResource>> getTime(UndervisningsgruppeResource undervisningsgruppe, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(undervisningsgruppe.getTime()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> timeService.getTimeResource(href, dfe)
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

    public CompletionStage<List<UndervisningsgruppemedlemskapResource>> getGruppemedlemskap(UndervisningsgruppeResource undervisningsgruppe, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(undervisningsgruppe.getGruppemedlemskap()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> undervisningsgruppemedlemskapService.getUndervisningsgruppemedlemskapResource(href, dfe)
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

