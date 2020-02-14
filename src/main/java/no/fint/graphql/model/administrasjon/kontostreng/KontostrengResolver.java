
package no.fint.graphql.model.administrasjon.kontostreng;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.administrasjon.aktivitet.AktivitetService;
import no.fint.graphql.model.administrasjon.anlegg.AnleggService;
import no.fint.graphql.model.administrasjon.ansvar.AnsvarService;
import no.fint.graphql.model.administrasjon.art.ArtService;
import no.fint.graphql.model.administrasjon.diverse.DiverseService;
import no.fint.graphql.model.administrasjon.funksjon.FunksjonService;
import no.fint.graphql.model.administrasjon.kontrakt.KontraktService;
import no.fint.graphql.model.administrasjon.lopenummer.LopenummerService;
import no.fint.graphql.model.administrasjon.objekt.ObjektService;
import no.fint.graphql.model.administrasjon.prosjekt.ProsjektService;
import no.fint.graphql.model.administrasjon.ramme.RammeService;


import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.kompleksedatatyper.KontostrengResource;
import no.fint.model.resource.administrasjon.kodeverk.AktivitetResource;
import no.fint.model.resource.administrasjon.kodeverk.AnleggResource;
import no.fint.model.resource.administrasjon.kodeverk.AnsvarResource;
import no.fint.model.resource.administrasjon.kodeverk.ArtResource;
import no.fint.model.resource.administrasjon.kodeverk.DiverseResource;
import no.fint.model.resource.administrasjon.kodeverk.FunksjonResource;
import no.fint.model.resource.administrasjon.kodeverk.KontraktResource;
import no.fint.model.resource.administrasjon.kodeverk.LopenummerResource;
import no.fint.model.resource.administrasjon.kodeverk.ObjektResource;
import no.fint.model.resource.administrasjon.kodeverk.ProsjektResource;
import no.fint.model.resource.administrasjon.kodeverk.RammeResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("administrasjonKontostrengResolver")
public class KontostrengResolver implements GraphQLResolver<KontostrengResource> {

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


    public CompletionStage<AktivitetResource> getAktivitet(KontostrengResource kontostreng, DataFetchingEnvironment dfe) {
        return Flux.fromStream(kontostreng.getAktivitet()
                .stream()
                .map(Link::getHref)
                .map(l -> aktivitetService.getAktivitetResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<AnleggResource> getAnlegg(KontostrengResource kontostreng, DataFetchingEnvironment dfe) {
        return Flux.fromStream(kontostreng.getAnlegg()
                .stream()
                .map(Link::getHref)
                .map(l -> anleggService.getAnleggResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<AnsvarResource> getAnsvar(KontostrengResource kontostreng, DataFetchingEnvironment dfe) {
        return Flux.fromStream(kontostreng.getAnsvar()
                .stream()
                .map(Link::getHref)
                .map(l -> ansvarService.getAnsvarResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<ArtResource> getArt(KontostrengResource kontostreng, DataFetchingEnvironment dfe) {
        return Flux.fromStream(kontostreng.getArt()
                .stream()
                .map(Link::getHref)
                .map(l -> artService.getArtResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<DiverseResource> getDiverse(KontostrengResource kontostreng, DataFetchingEnvironment dfe) {
        return Flux.fromStream(kontostreng.getDiverse()
                .stream()
                .map(Link::getHref)
                .map(l -> diverseService.getDiverseResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<FunksjonResource> getFunksjon(KontostrengResource kontostreng, DataFetchingEnvironment dfe) {
        return Flux.fromStream(kontostreng.getFunksjon()
                .stream()
                .map(Link::getHref)
                .map(l -> funksjonService.getFunksjonResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<KontraktResource> getKontrakt(KontostrengResource kontostreng, DataFetchingEnvironment dfe) {
        return Flux.fromStream(kontostreng.getKontrakt()
                .stream()
                .map(Link::getHref)
                .map(l -> kontraktService.getKontraktResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<LopenummerResource> getLopenummer(KontostrengResource kontostreng, DataFetchingEnvironment dfe) {
        return Flux.fromStream(kontostreng.getLopenummer()
                .stream()
                .map(Link::getHref)
                .map(l -> lopenummerService.getLopenummerResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<ObjektResource> getObjekt(KontostrengResource kontostreng, DataFetchingEnvironment dfe) {
        return Flux.fromStream(kontostreng.getObjekt()
                .stream()
                .map(Link::getHref)
                .map(l -> objektService.getObjektResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<ProsjektResource> getProsjekt(KontostrengResource kontostreng, DataFetchingEnvironment dfe) {
        return Flux.fromStream(kontostreng.getProsjekt()
                .stream()
                .map(Link::getHref)
                .map(l -> prosjektService.getProsjektResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

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

