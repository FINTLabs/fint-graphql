
package no.fint.graphql.model.model.karakterhistorie;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.karakterstatus.KarakterstatusService;
import no.fint.graphql.model.model.karakterverdi.KarakterverdiService;
import no.fint.graphql.model.model.skoleressurs.SkoleressursService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.utdanning.elev.SkoleressursResource;
import no.novari.fint.model.resource.utdanning.kodeverk.KarakterstatusResource;
import no.novari.fint.model.resource.utdanning.vurdering.KarakterhistorieResource;
import no.novari.fint.model.resource.utdanning.vurdering.KarakterverdiResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelKarakterhistorieResolver")
public class KarakterhistorieResolver {

    @Autowired
    private SkoleressursService skoleressursService;

    @Autowired
    private KarakterverdiService karakterverdiService;

    @Autowired
    private KarakterstatusService karakterstatusService;


    @SchemaMapping(typeName = "Karakterhistorie", field = "oppdatertAv")
    public CompletionStage<SkoleressursResource> getOppdatertAv(KarakterhistorieResource karakterhistorie, DataFetchingEnvironment dfe) {
        return Flux.fromStream(karakterhistorie.getOppdatertAv()
                .stream()
                .map(Link::getHref)
                .map(l -> skoleressursService.getSkoleressursResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Karakterhistorie", field = "opprinneligKarakterverdi")
    public CompletionStage<KarakterverdiResource> getOpprinneligKarakterverdi(KarakterhistorieResource karakterhistorie, DataFetchingEnvironment dfe) {
        return Flux.fromStream(karakterhistorie.getOpprinneligKarakterverdi()
                .stream()
                .map(Link::getHref)
                .map(l -> karakterverdiService.getKarakterverdiResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Karakterhistorie", field = "opprinneligKarakterstatus")
    public CompletionStage<KarakterstatusResource> getOpprinneligKarakterstatus(KarakterhistorieResource karakterhistorie, DataFetchingEnvironment dfe) {
        return Flux.fromStream(karakterhistorie.getOpprinneligKarakterstatus()
                .stream()
                .map(Link::getHref)
                .map(l -> karakterstatusService.getKarakterstatusResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Karakterhistorie", field = "karakterverdi")
    public CompletionStage<KarakterverdiResource> getKarakterverdi(KarakterhistorieResource karakterhistorie, DataFetchingEnvironment dfe) {
        return Flux.fromStream(karakterhistorie.getKarakterverdi()
                .stream()
                .map(Link::getHref)
                .map(l -> karakterverdiService.getKarakterverdiResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Karakterhistorie", field = "karakterstatus")
    public CompletionStage<KarakterstatusResource> getKarakterstatus(KarakterhistorieResource karakterhistorie, DataFetchingEnvironment dfe) {
        return Flux.fromStream(karakterhistorie.getKarakterstatus()
                .stream()
                .map(Link::getHref)
                .map(l -> karakterstatusService.getKarakterstatusResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

}

