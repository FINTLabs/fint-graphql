
package no.fint.graphql.model.administrasjon.prosjektart;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.administrasjon.prosjektart.ProsjektartService;
import no.fint.graphql.model.administrasjon.prosjekt.ProsjektService;
import no.fint.graphql.model.administrasjon.fullmakt.FullmaktService;


import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.kodeverk.ProsjektartResource;
import no.fint.model.resource.administrasjon.kodeverk.ProsjektartResource;
import no.fint.model.resource.administrasjon.kodeverk.ProsjektResource;
import no.fint.model.resource.administrasjon.fullmakt.FullmaktResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("administrasjonProsjektartResolver")
public class ProsjektartResolver implements GraphQLResolver<ProsjektartResource> {

    @Autowired
    private ProsjektartService prosjektartService;

    @Autowired
    private ProsjektService prosjektService;

    @Autowired
    private FullmaktService fullmaktService;


    public CompletionStage<List<ProsjektartResource>> getUnderordnet(ProsjektartResource prosjektart, DataFetchingEnvironment dfe) {
        return Flux.fromStream(prosjektart.getUnderordnet()
                .stream()
                .map(Link::getHref)
                .map(l -> prosjektartService.getProsjektartResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<ProsjektResource> getProsjekt(ProsjektartResource prosjektart, DataFetchingEnvironment dfe) {
        return Flux.fromStream(prosjektart.getProsjekt()
                .stream()
                .map(Link::getHref)
                .map(l -> prosjektService.getProsjektResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<ProsjektartResource> getOverordnet(ProsjektartResource prosjektart, DataFetchingEnvironment dfe) {
        return Flux.fromStream(prosjektart.getOverordnet()
                .stream()
                .map(Link::getHref)
                .map(l -> prosjektartService.getProsjektartResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<List<FullmaktResource>> getFullmakt(ProsjektartResource prosjektart, DataFetchingEnvironment dfe) {
        return Flux.fromStream(prosjektart.getFullmakt()
                .stream()
                .map(Link::getHref)
                .map(l -> fullmaktService.getFullmaktResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

}

