
package no.fint.graphql.model.model.fullmakt;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.aktivitet.AktivitetService;
import no.fint.graphql.model.model.anlegg.AnleggService;
import no.fint.graphql.model.model.ansvar.AnsvarService;
import no.fint.graphql.model.model.art.ArtService;
import no.fint.graphql.model.model.diverse.DiverseService;
import no.fint.graphql.model.model.formal.FormalService;
import no.fint.graphql.model.model.funksjon.FunksjonService;
import no.fint.graphql.model.model.kontrakt.KontraktService;
import no.fint.graphql.model.model.lopenummer.LopenummerService;
import no.fint.graphql.model.model.objekt.ObjektService;
import no.fint.graphql.model.model.organisasjonselement.OrganisasjonselementService;
import no.fint.graphql.model.model.personalressurs.PersonalressursService;
import no.fint.graphql.model.model.prosjekt.ProsjektService;
import no.fint.graphql.model.model.ramme.RammeService;
import no.fint.graphql.model.model.rolle.RolleService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.administrasjon.fullmakt.FullmaktResource;
import no.novari.fint.model.resource.administrasjon.fullmakt.RolleResource;
import no.novari.fint.model.resource.administrasjon.kodeverk.*;
import no.novari.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource;
import no.novari.fint.model.resource.administrasjon.personal.PersonalressursResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelFullmaktResolver")
public class FullmaktResolver implements GraphQLResolver<FullmaktResource> {

    @Autowired
    private RammeService rammeService;

    @Autowired
    private FunksjonService funksjonService;

    @Autowired
    private ObjektService objektService;

    @Autowired
    private OrganisasjonselementService organisasjonselementService;

    @Autowired
    private ArtService artService;

    @Autowired
    private AnleggService anleggService;

    @Autowired
    private DiverseService diverseService;

    @Autowired
    private AktivitetService aktivitetService;

    @Autowired
    private AnsvarService ansvarService;

    @Autowired
    private PersonalressursService personalressursService;

    @Autowired
    private KontraktService kontraktService;

    @Autowired
    private ProsjektService prosjektService;

    @Autowired
    private FormalService formalService;

    @Autowired
    private RolleService rolleService;

    @Autowired
    private LopenummerService lopenummerService;


    public CompletionStage<RammeResource> getRamme(FullmaktResource fullmakt, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fullmakt.getRamme()
                .stream()
                .map(Link::getHref)
                .map(l -> rammeService.getRammeResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<FunksjonResource> getFunksjon(FullmaktResource fullmakt, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fullmakt.getFunksjon()
                .stream()
                .map(Link::getHref)
                .map(l -> funksjonService.getFunksjonResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<ObjektResource> getObjekt(FullmaktResource fullmakt, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fullmakt.getObjekt()
                .stream()
                .map(Link::getHref)
                .map(l -> objektService.getObjektResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<OrganisasjonselementResource> getOrganisasjonselement(FullmaktResource fullmakt, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fullmakt.getOrganisasjonselement()
                .stream()
                .map(Link::getHref)
                .map(l -> organisasjonselementService.getOrganisasjonselementResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<ArtResource> getArt(FullmaktResource fullmakt, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fullmakt.getArt()
                .stream()
                .map(Link::getHref)
                .map(l -> artService.getArtResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<AnleggResource> getAnlegg(FullmaktResource fullmakt, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fullmakt.getAnlegg()
                .stream()
                .map(Link::getHref)
                .map(l -> anleggService.getAnleggResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<DiverseResource> getDiverse(FullmaktResource fullmakt, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fullmakt.getDiverse()
                .stream()
                .map(Link::getHref)
                .map(l -> diverseService.getDiverseResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<AktivitetResource> getAktivitet(FullmaktResource fullmakt, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fullmakt.getAktivitet()
                .stream()
                .map(Link::getHref)
                .map(l -> aktivitetService.getAktivitetResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<AnsvarResource> getAnsvar(FullmaktResource fullmakt, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fullmakt.getAnsvar()
                .stream()
                .map(Link::getHref)
                .map(l -> ansvarService.getAnsvarResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<PersonalressursResource> getStedfortreder(FullmaktResource fullmakt, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fullmakt.getStedfortreder()
                .stream()
                .map(Link::getHref)
                .map(l -> personalressursService.getPersonalressursResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<KontraktResource> getKontrakt(FullmaktResource fullmakt, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fullmakt.getKontrakt()
                .stream()
                .map(Link::getHref)
                .map(l -> kontraktService.getKontraktResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<PersonalressursResource> getFullmektig(FullmaktResource fullmakt, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fullmakt.getFullmektig()
                .stream()
                .map(Link::getHref)
                .map(l -> personalressursService.getPersonalressursResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<ProsjektResource> getProsjekt(FullmaktResource fullmakt, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fullmakt.getProsjekt()
                .stream()
                .map(Link::getHref)
                .map(l -> prosjektService.getProsjektResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<FormalResource> getFormal(FullmaktResource fullmakt, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fullmakt.getFormal()
                .stream()
                .map(Link::getHref)
                .map(l -> formalService.getFormalResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<RolleResource> getRolle(FullmaktResource fullmakt, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fullmakt.getRolle()
                .stream()
                .map(Link::getHref)
                .map(l -> rolleService.getRolleResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<LopenummerResource> getLopenummer(FullmaktResource fullmakt, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fullmakt.getLopenummer()
                .stream()
                .map(Link::getHref)
                .map(l -> lopenummerService.getLopenummerResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

}

