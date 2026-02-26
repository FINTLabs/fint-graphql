
package no.fint.graphql.model.model.undervisningsforhold;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.arbeidsforhold.ArbeidsforholdService;
import no.fint.graphql.model.model.eksamensgruppe.EksamensgruppeService;
import no.fint.graphql.model.model.klasse.KlasseService;
import no.fint.graphql.model.model.kontaktlarergruppe.KontaktlarergruppeService;
import no.fint.graphql.model.model.skole.SkoleService;
import no.fint.graphql.model.model.skoleressurs.SkoleressursService;
import no.fint.graphql.model.model.time.TimeService;
import no.fint.graphql.model.model.undervisningsgruppe.UndervisningsgruppeService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.administrasjon.personal.ArbeidsforholdResource;
import no.novari.fint.model.resource.utdanning.elev.KlasseResource;
import no.novari.fint.model.resource.utdanning.elev.KontaktlarergruppeResource;
import no.novari.fint.model.resource.utdanning.elev.SkoleressursResource;
import no.novari.fint.model.resource.utdanning.elev.UndervisningsforholdResource;
import no.novari.fint.model.resource.utdanning.timeplan.TimeResource;
import no.novari.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource;
import no.novari.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.novari.fint.model.resource.utdanning.vurdering.EksamensgruppeResource;
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

@Component("modelUndervisningsforholdResolver")
public class UndervisningsforholdResolver implements GraphQLResolver<UndervisningsforholdResource> {

    @Autowired
    private ArbeidsforholdService arbeidsforholdService;

    @Autowired
    private KlasseService klasseService;

    @Autowired
    private TimeService timeService;

    @Autowired
    private SkoleService skoleService;

    @Autowired
    private KontaktlarergruppeService kontaktlarergruppeService;

    @Autowired
    private SkoleressursService skoleressursService;

    @Autowired
    private UndervisningsgruppeService undervisningsgruppeService;

    @Autowired
    private EksamensgruppeService eksamensgruppeService;


    public CompletionStage<ArbeidsforholdResource> getArbeidsforhold(UndervisningsforholdResource undervisningsforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(undervisningsforhold.getArbeidsforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> arbeidsforholdService.getArbeidsforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<List<KlasseResource>> getKlasse(UndervisningsforholdResource undervisningsforhold, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(undervisningsforhold.getKlasse()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> klasseService.getKlasseResource(href, dfe)
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

    public CompletionStage<List<TimeResource>> getTime(UndervisningsforholdResource undervisningsforhold, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(undervisningsforhold.getTime()).orElseGet(List::of);
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

    public CompletionStage<SkoleResource> getSkole(UndervisningsforholdResource undervisningsforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(undervisningsforhold.getSkole()
                .stream()
                .map(Link::getHref)
                .map(l -> skoleService.getSkoleResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<List<KontaktlarergruppeResource>> getKontaktlarergruppe(UndervisningsforholdResource undervisningsforhold, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(undervisningsforhold.getKontaktlarergruppe()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> kontaktlarergruppeService.getKontaktlarergruppeResource(href, dfe)
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

    public CompletionStage<SkoleressursResource> getSkoleressurs(UndervisningsforholdResource undervisningsforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(undervisningsforhold.getSkoleressurs()
                .stream()
                .map(Link::getHref)
                .map(l -> skoleressursService.getSkoleressursResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<List<UndervisningsgruppeResource>> getUndervisningsgruppe(UndervisningsforholdResource undervisningsforhold, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(undervisningsforhold.getUndervisningsgruppe()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> undervisningsgruppeService.getUndervisningsgruppeResource(href, dfe)
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

    public CompletionStage<List<EksamensgruppeResource>> getEksamensgruppe(UndervisningsforholdResource undervisningsforhold, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(undervisningsforhold.getEksamensgruppe()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> eksamensgruppeService.getEksamensgruppeResource(href, dfe)
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

