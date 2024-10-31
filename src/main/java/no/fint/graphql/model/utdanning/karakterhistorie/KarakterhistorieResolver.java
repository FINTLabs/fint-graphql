
package no.fint.graphql.model.utdanning.karakterhistorie;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.utdanning.skoleressurs.SkoleressursService;
import no.fint.graphql.model.utdanning.karakterverdi.KarakterverdiService;
import no.fint.graphql.model.utdanning.karakterstatus.KarakterstatusService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.vurdering.KarakterhistorieResource;
import no.fint.model.resource.utdanning.elev.SkoleressursResource;
import no.fint.model.resource.utdanning.vurdering.KarakterverdiResource;
import no.fint.model.resource.utdanning.kodeverk.KarakterstatusResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("utdanningKarakterhistorieResolver")
public class KarakterhistorieResolver implements GraphQLResolver<KarakterhistorieResource> {

    @Autowired
    private SkoleressursService skoleressursService;

    @Autowired
    private KarakterverdiService karakterverdiService;

    @Autowired
    private KarakterstatusService karakterstatusService;


    public CompletionStage<SkoleressursResource> getOppdatertAv(KarakterhistorieResource karakterhistorie, DataFetchingEnvironment dfe) {
        return Flux.fromStream(karakterhistorie.getOppdatertAv()
                .stream()
                .map(Link::getHref)
                .map(l -> skoleressursService.getSkoleressursResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<KarakterverdiResource> getOpprinneligKarakterverdi(KarakterhistorieResource karakterhistorie, DataFetchingEnvironment dfe) {
        return Flux.fromStream(karakterhistorie.getOpprinneligKarakterverdi()
                .stream()
                .map(Link::getHref)
                .map(l -> karakterverdiService.getKarakterverdiResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<KarakterstatusResource> getOpprinneligKarakterstatus(KarakterhistorieResource karakterhistorie, DataFetchingEnvironment dfe) {
        return Flux.fromStream(karakterhistorie.getOpprinneligKarakterstatus()
                .stream()
                .map(Link::getHref)
                .map(l -> karakterstatusService.getKarakterstatusResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<KarakterverdiResource> getKarakterverdi(KarakterhistorieResource karakterhistorie, DataFetchingEnvironment dfe) {
        return Flux.fromStream(karakterhistorie.getKarakterverdi()
                .stream()
                .map(Link::getHref)
                .map(l -> karakterverdiService.getKarakterverdiResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

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

