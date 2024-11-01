
package no.fint.graphql.model.administrasjon.arbeidsforhold;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.administrasjon.aktivitet.AktivitetService;
import no.fint.graphql.model.administrasjon.anlegg.AnleggService;
import no.fint.graphql.model.administrasjon.ansvar.AnsvarService;
import no.fint.graphql.model.administrasjon.arbeidsforholdstype.ArbeidsforholdstypeService;
import no.fint.graphql.model.administrasjon.art.ArtService;
import no.fint.graphql.model.administrasjon.diverse.DiverseService;
import no.fint.graphql.model.administrasjon.formal.FormalService;
import no.fint.graphql.model.administrasjon.funksjon.FunksjonService;
import no.fint.graphql.model.administrasjon.kontrakt.KontraktService;
import no.fint.graphql.model.administrasjon.lopenummer.LopenummerService;
import no.fint.graphql.model.administrasjon.objekt.ObjektService;
import no.fint.graphql.model.administrasjon.prosjekt.ProsjektService;
import no.fint.graphql.model.administrasjon.ramme.RammeService;
import no.fint.graphql.model.administrasjon.stillingskode.StillingskodeService;
import no.fint.graphql.model.administrasjon.uketimetall.UketimetallService;
import no.fint.graphql.model.administrasjon.arbeidslokasjon.ArbeidslokasjonService;
import no.fint.graphql.model.administrasjon.organisasjonselement.OrganisasjonselementService;
import no.fint.graphql.model.administrasjon.personalressurs.PersonalressursService;
import no.fint.graphql.model.administrasjon.fastlonn.FastlonnService;
import no.fint.graphql.model.administrasjon.fasttillegg.FasttilleggService;
import no.fint.graphql.model.administrasjon.variabellonn.VariabellonnService;
import no.fint.graphql.model.utdanning.undervisningsforhold.UndervisningsforholdService;


import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.personal.ArbeidsforholdResource;
import no.fint.model.resource.administrasjon.kodeverk.AktivitetResource;
import no.fint.model.resource.administrasjon.kodeverk.AnleggResource;
import no.fint.model.resource.administrasjon.kodeverk.AnsvarResource;
import no.fint.model.resource.administrasjon.kodeverk.ArbeidsforholdstypeResource;
import no.fint.model.resource.administrasjon.kodeverk.ArtResource;
import no.fint.model.resource.administrasjon.kodeverk.DiverseResource;
import no.fint.model.resource.administrasjon.kodeverk.FormalResource;
import no.fint.model.resource.administrasjon.kodeverk.FunksjonResource;
import no.fint.model.resource.administrasjon.kodeverk.KontraktResource;
import no.fint.model.resource.administrasjon.kodeverk.LopenummerResource;
import no.fint.model.resource.administrasjon.kodeverk.ObjektResource;
import no.fint.model.resource.administrasjon.kodeverk.ProsjektResource;
import no.fint.model.resource.administrasjon.kodeverk.RammeResource;
import no.fint.model.resource.administrasjon.kodeverk.StillingskodeResource;
import no.fint.model.resource.administrasjon.kodeverk.UketimetallResource;
import no.fint.model.resource.administrasjon.organisasjon.ArbeidslokasjonResource;
import no.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.administrasjon.personal.FastlonnResource;
import no.fint.model.resource.administrasjon.personal.FasttilleggResource;
import no.fint.model.resource.administrasjon.personal.VariabellonnResource;
import no.fint.model.resource.utdanning.elev.UndervisningsforholdResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("administrasjonArbeidsforholdResolver")
public class ArbeidsforholdResolver implements GraphQLResolver<ArbeidsforholdResource> {

    @Autowired
    private AktivitetService aktivitetService;

    @Autowired
    private AnleggService anleggService;

    @Autowired
    private AnsvarService ansvarService;

    @Autowired
    private ArbeidsforholdstypeService arbeidsforholdstypeService;

    @Autowired
    private ArtService artService;

    @Autowired
    private DiverseService diverseService;

    @Autowired
    private FormalService formalService;

    @Autowired
    private FunksjonService funksjonService;

    @Autowired
    private KontraktService kontraktService;

    @Autowired
    private LopenummerService lopenummerService;

    @Autowired
    private ObjektService objektService;

    @Autowired
    private ProsjektService prosjektService;

    @Autowired
    private RammeService rammeService;

    @Autowired
    private StillingskodeService stillingskodeService;

    @Autowired
    private UketimetallService uketimetallService;

    @Autowired
    private ArbeidslokasjonService arbeidslokasjonService;

    @Autowired
    private OrganisasjonselementService organisasjonselementService;

    @Autowired
    private PersonalressursService personalressursService;

    @Autowired
    private FastlonnService fastlonnService;

    @Autowired
    private FasttilleggService fasttilleggService;

    @Autowired
    private VariabellonnService variabellonnService;

    @Autowired
    private UndervisningsforholdService undervisningsforholdService;


    public CompletionStage<AktivitetResource> getAktivitet(ArbeidsforholdResource arbeidsforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(arbeidsforhold.getAktivitet()
                .stream()
                .map(Link::getHref)
                .map(l -> aktivitetService.getAktivitetResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<AnleggResource> getAnlegg(ArbeidsforholdResource arbeidsforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(arbeidsforhold.getAnlegg()
                .stream()
                .map(Link::getHref)
                .map(l -> anleggService.getAnleggResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<AnsvarResource> getAnsvar(ArbeidsforholdResource arbeidsforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(arbeidsforhold.getAnsvar()
                .stream()
                .map(Link::getHref)
                .map(l -> ansvarService.getAnsvarResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<ArbeidsforholdstypeResource> getArbeidsforholdstype(ArbeidsforholdResource arbeidsforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(arbeidsforhold.getArbeidsforholdstype()
                .stream()
                .map(Link::getHref)
                .map(l -> arbeidsforholdstypeService.getArbeidsforholdstypeResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<ArtResource> getArt(ArbeidsforholdResource arbeidsforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(arbeidsforhold.getArt()
                .stream()
                .map(Link::getHref)
                .map(l -> artService.getArtResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<DiverseResource> getDiverse(ArbeidsforholdResource arbeidsforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(arbeidsforhold.getDiverse()
                .stream()
                .map(Link::getHref)
                .map(l -> diverseService.getDiverseResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<FormalResource> getFormal(ArbeidsforholdResource arbeidsforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(arbeidsforhold.getFormal()
                .stream()
                .map(Link::getHref)
                .map(l -> formalService.getFormalResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<FunksjonResource> getFunksjon(ArbeidsforholdResource arbeidsforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(arbeidsforhold.getFunksjon()
                .stream()
                .map(Link::getHref)
                .map(l -> funksjonService.getFunksjonResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<KontraktResource> getKontrakt(ArbeidsforholdResource arbeidsforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(arbeidsforhold.getKontrakt()
                .stream()
                .map(Link::getHref)
                .map(l -> kontraktService.getKontraktResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<LopenummerResource> getLopenummer(ArbeidsforholdResource arbeidsforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(arbeidsforhold.getLopenummer()
                .stream()
                .map(Link::getHref)
                .map(l -> lopenummerService.getLopenummerResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<ObjektResource> getObjekt(ArbeidsforholdResource arbeidsforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(arbeidsforhold.getObjekt()
                .stream()
                .map(Link::getHref)
                .map(l -> objektService.getObjektResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<ProsjektResource> getProsjekt(ArbeidsforholdResource arbeidsforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(arbeidsforhold.getProsjekt()
                .stream()
                .map(Link::getHref)
                .map(l -> prosjektService.getProsjektResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<RammeResource> getRamme(ArbeidsforholdResource arbeidsforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(arbeidsforhold.getRamme()
                .stream()
                .map(Link::getHref)
                .map(l -> rammeService.getRammeResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<StillingskodeResource> getStillingskode(ArbeidsforholdResource arbeidsforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(arbeidsforhold.getStillingskode()
                .stream()
                .map(Link::getHref)
                .map(l -> stillingskodeService.getStillingskodeResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<UketimetallResource> getTimerPerUke(ArbeidsforholdResource arbeidsforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(arbeidsforhold.getTimerPerUke()
                .stream()
                .map(Link::getHref)
                .map(l -> uketimetallService.getUketimetallResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<ArbeidslokasjonResource> getArbeidslokasjon(ArbeidsforholdResource arbeidsforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(arbeidsforhold.getArbeidslokasjon()
                .stream()
                .map(Link::getHref)
                .map(l -> arbeidslokasjonService.getArbeidslokasjonResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<OrganisasjonselementResource> getArbeidssted(ArbeidsforholdResource arbeidsforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(arbeidsforhold.getArbeidssted()
                .stream()
                .map(Link::getHref)
                .map(l -> organisasjonselementService.getOrganisasjonselementResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<PersonalressursResource> getPersonalleder(ArbeidsforholdResource arbeidsforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(arbeidsforhold.getPersonalleder()
                .stream()
                .map(Link::getHref)
                .map(l -> personalressursService.getPersonalressursResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<List<FastlonnResource>> getFastlonn(ArbeidsforholdResource arbeidsforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(arbeidsforhold.getFastlonn()
                .stream()
                .map(Link::getHref)
                .map(l -> fastlonnService.getFastlonnResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<FasttilleggResource>> getFasttillegg(ArbeidsforholdResource arbeidsforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(arbeidsforhold.getFasttillegg()
                .stream()
                .map(Link::getHref)
                .map(l -> fasttilleggService.getFasttilleggResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<VariabellonnResource>> getVariabellonn(ArbeidsforholdResource arbeidsforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(arbeidsforhold.getVariabellonn()
                .stream()
                .map(Link::getHref)
                .map(l -> variabellonnService.getVariabellonnResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<PersonalressursResource> getPersonalressurs(ArbeidsforholdResource arbeidsforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(arbeidsforhold.getPersonalressurs()
                .stream()
                .map(Link::getHref)
                .map(l -> personalressursService.getPersonalressursResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<UndervisningsforholdResource> getUndervisningsforhold(ArbeidsforholdResource arbeidsforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(arbeidsforhold.getUndervisningsforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> undervisningsforholdService.getUndervisningsforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

}

