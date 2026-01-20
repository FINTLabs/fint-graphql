
package no.fint.graphql.model.model.elevforhold;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.model.elev.ElevService;
import no.fint.graphql.model.model.elevkategori.ElevkategoriService;
import no.fint.graphql.model.model.skole.SkoleService;
import no.fint.graphql.model.model.avbruddsarsak.AvbruddsarsakService;
import no.fint.graphql.model.model.elevfravar.ElevfravarService;
import no.fint.graphql.model.model.faggruppemedlemskap.FaggruppemedlemskapService;
import no.fint.graphql.model.model.skolear.SkolearService;
import no.fint.graphql.model.model.undervisningsgruppemedlemskap.UndervisningsgruppemedlemskapService;
import no.fint.graphql.model.model.persongruppemedlemskap.PersongruppemedlemskapService;
import no.fint.graphql.model.model.eksamensgruppemedlemskap.EksamensgruppemedlemskapService;
import no.fint.graphql.model.model.kontaktlarergruppemedlemskap.KontaktlarergruppemedlemskapService;
import no.fint.graphql.model.model.fravarsoversikt.FravarsoversiktService;
import no.fint.graphql.model.model.elevtilrettelegging.ElevtilretteleggingService;
import no.fint.graphql.model.model.elevvurdering.ElevvurderingService;
import no.fint.graphql.model.model.programomrademedlemskap.ProgramomrademedlemskapService;
import no.fint.graphql.model.model.klassemedlemskap.KlassemedlemskapService;


import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.novari.fint.model.resource.utdanning.elev.ElevResource;
import no.novari.fint.model.resource.utdanning.kodeverk.ElevkategoriResource;
import no.novari.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.novari.fint.model.resource.utdanning.kodeverk.AvbruddsarsakResource;
import no.novari.fint.model.resource.utdanning.vurdering.ElevfravarResource;
import no.novari.fint.model.resource.utdanning.timeplan.FaggruppemedlemskapResource;
import no.novari.fint.model.resource.utdanning.kodeverk.SkolearResource;
import no.novari.fint.model.resource.utdanning.timeplan.UndervisningsgruppemedlemskapResource;
import no.novari.fint.model.resource.utdanning.elev.PersongruppemedlemskapResource;
import no.novari.fint.model.resource.utdanning.vurdering.EksamensgruppemedlemskapResource;
import no.novari.fint.model.resource.utdanning.elev.KontaktlarergruppemedlemskapResource;
import no.novari.fint.model.resource.utdanning.vurdering.FravarsoversiktResource;
import no.novari.fint.model.resource.utdanning.elev.ElevtilretteleggingResource;
import no.novari.fint.model.resource.utdanning.vurdering.ElevvurderingResource;
import no.novari.fint.model.resource.utdanning.utdanningsprogram.ProgramomrademedlemskapResource;
import no.novari.fint.model.resource.utdanning.elev.KlassemedlemskapResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

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
        return Flux.fromStream(elevforhold.getAvbruddsarsak()
                .stream()
                .map(Link::getHref)
                .map(l -> avbruddsarsakService.getAvbruddsarsakResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
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
        return Flux.fromStream(elevforhold.getFaggruppemedlemskap()
                .stream()
                .map(Link::getHref)
                .map(l -> faggruppemedlemskapService.getFaggruppemedlemskapResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
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
        return Flux.fromStream(elevforhold.getUndervisningsgruppemedlemskap()
                .stream()
                .map(Link::getHref)
                .map(l -> undervisningsgruppemedlemskapService.getUndervisningsgruppemedlemskapResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<PersongruppemedlemskapResource>> getPersongruppemedlemskap(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevforhold.getPersongruppemedlemskap()
                .stream()
                .map(Link::getHref)
                .map(l -> persongruppemedlemskapService.getPersongruppemedlemskapResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<EksamensgruppemedlemskapResource>> getEksamensgruppemedlemskap(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevforhold.getEksamensgruppemedlemskap()
                .stream()
                .map(Link::getHref)
                .map(l -> eksamensgruppemedlemskapService.getEksamensgruppemedlemskapResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<KontaktlarergruppemedlemskapResource>> getKontaktlarergruppemedlemskap(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevforhold.getKontaktlarergruppemedlemskap()
                .stream()
                .map(Link::getHref)
                .map(l -> kontaktlarergruppemedlemskapService.getKontaktlarergruppemedlemskapResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<FravarsoversiktResource>> getElevfravar(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevforhold.getElevfravar()
                .stream()
                .map(Link::getHref)
                .map(l -> fravarsoversiktService.getFravarsoversiktResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<ElevtilretteleggingResource>> getTilrettelegging(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevforhold.getTilrettelegging()
                .stream()
                .map(Link::getHref)
                .map(l -> elevtilretteleggingService.getElevtilretteleggingResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
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
        return Flux.fromStream(elevforhold.getProgramomrademedlemskap()
                .stream()
                .map(Link::getHref)
                .map(l -> programomrademedlemskapService.getProgramomrademedlemskapResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<KlassemedlemskapResource>> getKlassemedlemskap(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevforhold.getKlassemedlemskap()
                .stream()
                .map(Link::getHref)
                .map(l -> klassemedlemskapService.getKlassemedlemskapResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

}

