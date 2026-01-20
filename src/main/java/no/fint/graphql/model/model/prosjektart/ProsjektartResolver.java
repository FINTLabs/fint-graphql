
package no.fint.graphql.model.model.prosjektart;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.model.prosjektart.ProsjektartService;
import no.fint.graphql.model.model.prosjekt.ProsjektService;


import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.administrasjon.kodeverk.ProsjektartResource;
import no.novari.fint.model.resource.administrasjon.kodeverk.ProsjektartResource;
import no.novari.fint.model.resource.administrasjon.kodeverk.ProsjektResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("modelProsjektartResolver")
public class ProsjektartResolver implements GraphQLResolver<ProsjektartResource> {

    @Autowired
    private ProsjektartService prosjektartService;

    @Autowired
    private ProsjektService prosjektService;


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

}

