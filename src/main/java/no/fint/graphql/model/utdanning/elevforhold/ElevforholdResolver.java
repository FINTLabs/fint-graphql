
package no.fint.graphql.model.utdanning.elevforhold;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.utdanning.elev.ElevService;
import no.fint.graphql.model.utdanning.fagmerknad.FagmerknadService;
import no.fint.graphql.model.utdanning.elevkategori.ElevkategoriService;
import no.fint.graphql.model.utdanning.skole.SkoleService;
import no.fint.graphql.model.utdanning.avbruddsarsak.AvbruddsarsakService;
import no.fint.graphql.model.utdanning.elevfravar.ElevfravarService;
import no.fint.graphql.model.utdanning.faggruppemedlemskap.FaggruppemedlemskapService;
import no.fint.graphql.model.utdanning.skolear.SkolearService;
import no.fint.graphql.model.utdanning.basisgruppe.BasisgruppeService;
import no.fint.graphql.model.utdanning.basisgruppemedlemskap.BasisgruppemedlemskapService;
import no.fint.graphql.model.utdanning.undervisningsgruppemedlemskap.UndervisningsgruppemedlemskapService;
import no.fint.graphql.model.utdanning.vurdering.VurderingService;
import no.fint.graphql.model.utdanning.sluttordensvurdering.SluttordensvurderingService;
import no.fint.graphql.model.utdanning.kontaktlarergruppe.KontaktlarergruppeService;
import no.fint.graphql.model.utdanning.underveisfagvurdering.UnderveisfagvurderingService;
import no.fint.graphql.model.utdanning.halvarsfagvurdering.HalvarsfagvurderingService;
import no.fint.graphql.model.utdanning.sluttfagvurdering.SluttfagvurderingService;
import no.fint.graphql.model.utdanning.persongruppemedlemskap.PersongruppemedlemskapService;
import no.fint.graphql.model.utdanning.eksamensgruppemedlemskap.EksamensgruppemedlemskapService;
import no.fint.graphql.model.utdanning.kontaktlarergruppemedlemskap.KontaktlarergruppemedlemskapService;
import no.fint.graphql.model.utdanning.fravarsoversikt.FravarsoversiktService;
import no.fint.graphql.model.utdanning.elevtilrettelegging.ElevtilretteleggingService;
import no.fint.graphql.model.utdanning.halvarsordensvurdering.HalvarsordensvurderingService;
import no.fint.graphql.model.utdanning.programomrade.ProgramomradeService;
import no.fint.graphql.model.utdanning.elevvurdering.ElevvurderingService;
import no.fint.graphql.model.utdanning.programomrademedlemskap.ProgramomrademedlemskapService;
import no.fint.graphql.model.utdanning.underveisordensvurdering.UnderveisordensvurderingService;
import no.fint.graphql.model.utdanning.eksamensgruppe.EksamensgruppeService;
import no.fint.graphql.model.utdanning.undervisningsgruppe.UndervisningsgruppeService;
import no.fint.graphql.model.utdanning.medlemskap.MedlemskapService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.fint.model.resource.utdanning.elev.ElevResource;
import no.fint.model.resource.utdanning.kodeverk.FagmerknadResource;
import no.fint.model.resource.utdanning.kodeverk.ElevkategoriResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.model.resource.utdanning.kodeverk.AvbruddsarsakResource;
import no.fint.model.resource.utdanning.vurdering.ElevfravarResource;
import no.fint.model.resource.utdanning.timeplan.FaggruppemedlemskapResource;
import no.fint.model.resource.utdanning.kodeverk.SkolearResource;
import no.fint.model.resource.utdanning.elev.BasisgruppeResource;
import no.fint.model.resource.utdanning.elev.BasisgruppemedlemskapResource;
import no.fint.model.resource.utdanning.timeplan.UndervisningsgruppemedlemskapResource;
import no.fint.model.resource.utdanning.vurdering.VurderingResource;
import no.fint.model.resource.utdanning.vurdering.SluttordensvurderingResource;
import no.fint.model.resource.utdanning.elev.KontaktlarergruppeResource;
import no.fint.model.resource.utdanning.vurdering.UnderveisfagvurderingResource;
import no.fint.model.resource.utdanning.vurdering.HalvarsfagvurderingResource;
import no.fint.model.resource.utdanning.vurdering.SluttfagvurderingResource;
import no.fint.model.resource.utdanning.elev.PersongruppemedlemskapResource;
import no.fint.model.resource.utdanning.vurdering.EksamensgruppemedlemskapResource;
import no.fint.model.resource.utdanning.elev.KontaktlarergruppemedlemskapResource;
import no.fint.model.resource.utdanning.vurdering.FravarsoversiktResource;
import no.fint.model.resource.utdanning.elev.ElevtilretteleggingResource;
import no.fint.model.resource.utdanning.vurdering.HalvarsordensvurderingResource;
import no.fint.model.resource.utdanning.utdanningsprogram.ProgramomradeResource;
import no.fint.model.resource.utdanning.vurdering.ElevvurderingResource;
import no.fint.model.resource.utdanning.utdanningsprogram.ProgramomrademedlemskapResource;
import no.fint.model.resource.utdanning.vurdering.UnderveisordensvurderingResource;
import no.fint.model.resource.utdanning.vurdering.EksamensgruppeResource;
import no.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource;
import no.fint.model.resource.utdanning.elev.MedlemskapResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("utdanningElevforholdResolver")
public class ElevforholdResolver implements GraphQLResolver<ElevforholdResource> {

    @Autowired
    private ElevService elevService;

    @Autowired
    private FagmerknadService fagmerknadService;

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
    private BasisgruppeService basisgruppeService;

    @Autowired
    private BasisgruppemedlemskapService basisgruppemedlemskapService;

    @Autowired
    private UndervisningsgruppemedlemskapService undervisningsgruppemedlemskapService;

    @Autowired
    private VurderingService vurderingService;

    @Autowired
    private SluttordensvurderingService sluttordensvurderingService;

    @Autowired
    private KontaktlarergruppeService kontaktlarergruppeService;

    @Autowired
    private UnderveisfagvurderingService underveisfagvurderingService;

    @Autowired
    private HalvarsfagvurderingService halvarsfagvurderingService;

    @Autowired
    private SluttfagvurderingService sluttfagvurderingService;

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
    private HalvarsordensvurderingService halvarsordensvurderingService;

    @Autowired
    private ProgramomradeService programomradeService;

    @Autowired
    private ElevvurderingService elevvurderingService;

    @Autowired
    private ProgramomrademedlemskapService programomrademedlemskapService;

    @Autowired
    private UnderveisordensvurderingService underveisordensvurderingService;

    @Autowired
    private EksamensgruppeService eksamensgruppeService;

    @Autowired
    private UndervisningsgruppeService undervisningsgruppeService;

    @Autowired
    private MedlemskapService medlemskapService;


    public CompletionStage<ElevResource> getElev(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevforhold.getElev()
                .stream()
                .map(Link::getHref)
                .map(l -> elevService.getElevResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<List<FagmerknadResource>> getSidemal(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevforhold.getSidemal()
                .stream()
                .map(Link::getHref)
                .map(l -> fagmerknadService.getFagmerknadResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
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

    public CompletionStage<FagmerknadResource> getKroppsoving(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevforhold.getKroppsoving()
                .stream()
                .map(Link::getHref)
                .map(l -> fagmerknadService.getFagmerknadResource(l, dfe)))
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

    public CompletionStage<List<BasisgruppeResource>> getBasisgruppe(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevforhold.getBasisgruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> basisgruppeService.getBasisgruppeResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<BasisgruppemedlemskapResource>> getBasisgruppemedlemskap(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevforhold.getBasisgruppemedlemskap()
                .stream()
                .map(Link::getHref)
                .map(l -> basisgruppemedlemskapService.getBasisgruppemedlemskapResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
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

    public CompletionStage<List<VurderingResource>> getVurdering(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevforhold.getVurdering()
                .stream()
                .map(Link::getHref)
                .map(l -> vurderingService.getVurderingResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<SluttordensvurderingResource>> getSluttordensvurdering(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevforhold.getSluttordensvurdering()
                .stream()
                .map(Link::getHref)
                .map(l -> sluttordensvurderingService.getSluttordensvurderingResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<KontaktlarergruppeResource>> getKontaktlarergruppe(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevforhold.getKontaktlarergruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> kontaktlarergruppeService.getKontaktlarergruppeResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<UnderveisfagvurderingResource>> getUnderveisfagvurdering(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevforhold.getUnderveisfagvurdering()
                .stream()
                .map(Link::getHref)
                .map(l -> underveisfagvurderingService.getUnderveisfagvurderingResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<HalvarsfagvurderingResource>> getHalvarsfagvurdering(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevforhold.getHalvarsfagvurdering()
                .stream()
                .map(Link::getHref)
                .map(l -> halvarsfagvurderingService.getHalvarsfagvurderingResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<SluttfagvurderingResource>> getSluttfagvurdering(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevforhold.getSluttfagvurdering()
                .stream()
                .map(Link::getHref)
                .map(l -> sluttfagvurderingService.getSluttfagvurderingResource(l, dfe)))
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

    public CompletionStage<List<HalvarsordensvurderingResource>> getHalvarsordensvurdering(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevforhold.getHalvarsordensvurdering()
                .stream()
                .map(Link::getHref)
                .map(l -> halvarsordensvurderingService.getHalvarsordensvurderingResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<ProgramomradeResource> getProgramomrade(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevforhold.getProgramomrade()
                .stream()
                .map(Link::getHref)
                .map(l -> programomradeService.getProgramomradeResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
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

    public CompletionStage<List<UnderveisordensvurderingResource>> getUnderveisordensvurdering(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevforhold.getUnderveisordensvurdering()
                .stream()
                .map(Link::getHref)
                .map(l -> underveisordensvurderingService.getUnderveisordensvurderingResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<EksamensgruppeResource>> getEksamensgruppe(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevforhold.getEksamensgruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> eksamensgruppeService.getEksamensgruppeResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<UndervisningsgruppeResource>> getUndervisningsgruppe(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevforhold.getUndervisningsgruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> undervisningsgruppeService.getUndervisningsgruppeResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<MedlemskapResource>> getMedlemskap(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevforhold.getMedlemskap()
                .stream()
                .map(Link::getHref)
                .map(l -> medlemskapService.getMedlemskapResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

}

