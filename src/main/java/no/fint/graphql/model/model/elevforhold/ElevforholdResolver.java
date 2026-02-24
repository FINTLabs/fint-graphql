
package no.fint.graphql.model.model.elevforhold;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.avbruddsarsak.AvbruddsarsakService;
import no.fint.graphql.model.model.eksamensgruppemedlemskap.EksamensgruppemedlemskapService;
import no.fint.graphql.model.model.elev.ElevService;
import no.fint.graphql.model.model.elevfravar.ElevfravarService;
import no.fint.graphql.model.model.elevkategori.ElevkategoriService;
import no.fint.graphql.model.model.elevtilrettelegging.ElevtilretteleggingService;
import no.fint.graphql.model.model.elevvurdering.ElevvurderingService;
import no.fint.graphql.model.model.faggruppemedlemskap.FaggruppemedlemskapService;
import no.fint.graphql.model.model.fravarsoversikt.FravarsoversiktService;
import no.fint.graphql.model.model.klassemedlemskap.KlassemedlemskapService;
import no.fint.graphql.model.model.kontaktlarergruppemedlemskap.KontaktlarergruppemedlemskapService;
import no.fint.graphql.model.model.persongruppemedlemskap.PersongruppemedlemskapService;
import no.fint.graphql.model.model.programomrademedlemskap.ProgramomrademedlemskapService;
import no.fint.graphql.model.model.skole.SkoleService;
import no.fint.graphql.model.model.skolear.SkolearService;
import no.fint.graphql.model.model.undervisningsgruppemedlemskap.UndervisningsgruppemedlemskapService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.utdanning.elev.*;
import no.novari.fint.model.resource.utdanning.kodeverk.AvbruddsarsakResource;
import no.novari.fint.model.resource.utdanning.kodeverk.ElevkategoriResource;
import no.novari.fint.model.resource.utdanning.kodeverk.SkolearResource;
import no.novari.fint.model.resource.utdanning.timeplan.FaggruppemedlemskapResource;
import no.novari.fint.model.resource.utdanning.timeplan.UndervisningsgruppemedlemskapResource;
import no.novari.fint.model.resource.utdanning.utdanningsprogram.ProgramomrademedlemskapResource;
import no.novari.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.novari.fint.model.resource.utdanning.vurdering.EksamensgruppemedlemskapResource;
import no.novari.fint.model.resource.utdanning.vurdering.ElevfravarResource;
import no.novari.fint.model.resource.utdanning.vurdering.ElevvurderingResource;
import no.novari.fint.model.resource.utdanning.vurdering.FravarsoversiktResource;
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

@Component("modelElevforholdResolver")
public class ElevforholdResolver implements GraphQLResolver<ElevforholdResource> {

    @Autowired
    private ElevService elevService;

    @Autowired
    private ElevkategoriService elevkategoriService;

    @Autowired
    private SkoleService skoleService;

    @Autowired
    private AvbruddsarsakService avbruddsarsakService;

    @Autowired
    private ElevfravarService elevfravarService;

    @Autowired
    private FaggruppemedlemskapService faggruppemedlemskapService;

    @Autowired
    private SkolearService skolearService;

    @Autowired
    private UndervisningsgruppemedlemskapService undervisningsgruppemedlemskapService;

    @Autowired
    private PersongruppemedlemskapService persongruppemedlemskapService;

    @Autowired
    private EksamensgruppemedlemskapService eksamensgruppemedlemskapService;

    @Autowired
    private KontaktlarergruppemedlemskapService kontaktlarergruppemedlemskapService;

    @Autowired
    private FravarsoversiktService fravarsoversiktService;

    @Autowired
    private ElevtilretteleggingService elevtilretteleggingService;

    @Autowired
    private ElevvurderingService elevvurderingService;

    @Autowired
    private ProgramomrademedlemskapService programomrademedlemskapService;

    @Autowired
    private KlassemedlemskapService klassemedlemskapService;


    public CompletionStage<ElevResource> getElev(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevforhold.getElev()
                .stream()
                .map(Link::getHref)
                .map(l -> elevService.getElevResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<ElevkategoriResource> getKategori(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevforhold.getKategori()
                .stream()
                .map(Link::getHref)
                .map(l -> elevkategoriService.getElevkategoriResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<SkoleResource> getSkole(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevforhold.getSkole()
                .stream()
                .map(Link::getHref)
                .map(l -> skoleService.getSkoleResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<List<AvbruddsarsakResource>> getAvbruddsarsak(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(elevforhold.getAvbruddsarsak()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> avbruddsarsakService.getAvbruddsarsakResource(href, dfe)
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

    public CompletionStage<ElevfravarResource> getFravarsregistreringer(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevforhold.getFravarsregistreringer()
                .stream()
                .map(Link::getHref)
                .map(l -> elevfravarService.getElevfravarResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<List<FaggruppemedlemskapResource>> getFaggruppemedlemskap(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(elevforhold.getFaggruppemedlemskap()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> faggruppemedlemskapService.getFaggruppemedlemskapResource(href, dfe)
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

    public CompletionStage<SkolearResource> getSkolear(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevforhold.getSkolear()
                .stream()
                .map(Link::getHref)
                .map(l -> skolearService.getSkolearResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<List<UndervisningsgruppemedlemskapResource>> getUndervisningsgruppemedlemskap(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(elevforhold.getUndervisningsgruppemedlemskap()).orElseGet(List::of);
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

    public CompletionStage<List<PersongruppemedlemskapResource>> getPersongruppemedlemskap(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(elevforhold.getPersongruppemedlemskap()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> persongruppemedlemskapService.getPersongruppemedlemskapResource(href, dfe)
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

    public CompletionStage<List<EksamensgruppemedlemskapResource>> getEksamensgruppemedlemskap(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(elevforhold.getEksamensgruppemedlemskap()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> eksamensgruppemedlemskapService.getEksamensgruppemedlemskapResource(href, dfe)
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

    public CompletionStage<List<KontaktlarergruppemedlemskapResource>> getKontaktlarergruppemedlemskap(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(elevforhold.getKontaktlarergruppemedlemskap()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> kontaktlarergruppemedlemskapService.getKontaktlarergruppemedlemskapResource(href, dfe)
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

    public CompletionStage<List<FravarsoversiktResource>> getElevfravar(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(elevforhold.getElevfravar()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> fravarsoversiktService.getFravarsoversiktResource(href, dfe)
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

    public CompletionStage<List<ElevtilretteleggingResource>> getTilrettelegging(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(elevforhold.getTilrettelegging()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> elevtilretteleggingService.getElevtilretteleggingResource(href, dfe)
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

    public CompletionStage<ElevvurderingResource> getElevvurdering(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevforhold.getElevvurdering()
                .stream()
                .map(Link::getHref)
                .map(l -> elevvurderingService.getElevvurderingResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<List<ProgramomrademedlemskapResource>> getProgramomrademedlemskap(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(elevforhold.getProgramomrademedlemskap()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> programomrademedlemskapService.getProgramomrademedlemskapResource(href, dfe)
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

    public CompletionStage<List<KlassemedlemskapResource>> getKlassemedlemskap(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(elevforhold.getKlassemedlemskap()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> klassemedlemskapService.getKlassemedlemskapResource(href, dfe)
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

