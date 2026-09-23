
package no.fint.graphql.model.model.kontostreng;

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
import no.fint.graphql.model.model.prosjekt.ProsjektService;
import no.fint.graphql.model.model.prosjektart.ProsjektartService;
import no.fint.graphql.model.model.ramme.RammeService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.administrasjon.kodeverk.*;
import no.novari.fint.model.resource.administrasjon.kompleksedatatyper.KontostrengResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelKontostrengResolver")
public class KontostrengResolver {

    @Autowired
    private AktivitetService aktivitetService;

    @Autowired
    private AnleggService anleggService;

    @Autowired
    private AnsvarService ansvarService;

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
    private ProsjektartService prosjektartService;

    @Autowired
    private RammeService rammeService;


    @SchemaMapping(typeName = "Kontostreng", field = "aktivitet")
    public CompletionStage<AktivitetResource> getAktivitet(KontostrengResource kontostreng, DataFetchingEnvironment dfe) {
        return Flux.fromStream(kontostreng.getAktivitet()
                .stream()
                .map(Link::getHref)
                .map(l -> aktivitetService.getAktivitetResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Kontostreng", field = "anlegg")
    public CompletionStage<AnleggResource> getAnlegg(KontostrengResource kontostreng, DataFetchingEnvironment dfe) {
        return Flux.fromStream(kontostreng.getAnlegg()
                .stream()
                .map(Link::getHref)
                .map(l -> anleggService.getAnleggResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Kontostreng", field = "ansvar")
    public CompletionStage<AnsvarResource> getAnsvar(KontostrengResource kontostreng, DataFetchingEnvironment dfe) {
        return Flux.fromStream(kontostreng.getAnsvar()
                .stream()
                .map(Link::getHref)
                .map(l -> ansvarService.getAnsvarResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Kontostreng", field = "art")
    public CompletionStage<ArtResource> getArt(KontostrengResource kontostreng, DataFetchingEnvironment dfe) {
        return Flux.fromStream(kontostreng.getArt()
                .stream()
                .map(Link::getHref)
                .map(l -> artService.getArtResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Kontostreng", field = "diverse")
    public CompletionStage<DiverseResource> getDiverse(KontostrengResource kontostreng, DataFetchingEnvironment dfe) {
        return Flux.fromStream(kontostreng.getDiverse()
                .stream()
                .map(Link::getHref)
                .map(l -> diverseService.getDiverseResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Kontostreng", field = "formal")
    public CompletionStage<FormalResource> getFormal(KontostrengResource kontostreng, DataFetchingEnvironment dfe) {
        return Flux.fromStream(kontostreng.getFormal()
                .stream()
                .map(Link::getHref)
                .map(l -> formalService.getFormalResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Kontostreng", field = "funksjon")
    public CompletionStage<FunksjonResource> getFunksjon(KontostrengResource kontostreng, DataFetchingEnvironment dfe) {
        return Flux.fromStream(kontostreng.getFunksjon()
                .stream()
                .map(Link::getHref)
                .map(l -> funksjonService.getFunksjonResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Kontostreng", field = "kontrakt")
    public CompletionStage<KontraktResource> getKontrakt(KontostrengResource kontostreng, DataFetchingEnvironment dfe) {
        return Flux.fromStream(kontostreng.getKontrakt()
                .stream()
                .map(Link::getHref)
                .map(l -> kontraktService.getKontraktResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Kontostreng", field = "lopenummer")
    public CompletionStage<LopenummerResource> getLopenummer(KontostrengResource kontostreng, DataFetchingEnvironment dfe) {
        return Flux.fromStream(kontostreng.getLopenummer()
                .stream()
                .map(Link::getHref)
                .map(l -> lopenummerService.getLopenummerResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Kontostreng", field = "objekt")
    public CompletionStage<ObjektResource> getObjekt(KontostrengResource kontostreng, DataFetchingEnvironment dfe) {
        return Flux.fromStream(kontostreng.getObjekt()
                .stream()
                .map(Link::getHref)
                .map(l -> objektService.getObjektResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Kontostreng", field = "prosjekt")
    public CompletionStage<ProsjektResource> getProsjekt(KontostrengResource kontostreng, DataFetchingEnvironment dfe) {
        return Flux.fromStream(kontostreng.getProsjekt()
                .stream()
                .map(Link::getHref)
                .map(l -> prosjektService.getProsjektResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Kontostreng", field = "prosjektart")
    public CompletionStage<ProsjektartResource> getProsjektart(KontostrengResource kontostreng, DataFetchingEnvironment dfe) {
        return Flux.fromStream(kontostreng.getProsjektart()
                .stream()
                .map(Link::getHref)
                .map(l -> prosjektartService.getProsjektartResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Kontostreng", field = "ramme")
    public CompletionStage<RammeResource> getRamme(KontostrengResource kontostreng, DataFetchingEnvironment dfe) {
        return Flux.fromStream(kontostreng.getRamme()
                .stream()
                .map(Link::getHref)
                .map(l -> rammeService.getRammeResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

}

