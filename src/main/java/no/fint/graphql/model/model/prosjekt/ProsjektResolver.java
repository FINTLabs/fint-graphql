
package no.fint.graphql.model.model.prosjekt;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.model.prosjektart.ProsjektartService;
import no.fint.graphql.model.model.fullmakt.FullmaktService;


import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.administrasjon.kodeverk.ProsjektResource;
import no.novari.fint.model.resource.administrasjon.kodeverk.ProsjektartResource;
import no.novari.fint.model.resource.administrasjon.fullmakt.FullmaktResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("modelProsjektResolver")
public class ProsjektResolver implements GraphQLResolver<ProsjektResource> {

    @Autowired
    private ProsjektartService prosjektartService;

    @Autowired
    private FullmaktService fullmaktService;


    public CompletionStage<List<ProsjektartResource>> getProsjektart(ProsjektResource prosjekt, DataFetchingEnvironment dfe) {
        return Flux.fromStream(prosjekt.getProsjektart()
                .stream()
                .map(Link::getHref)
                .map(l -> prosjektartService.getProsjektartResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<FullmaktResource>> getFullmakt(ProsjektResource prosjekt, DataFetchingEnvironment dfe) {
        return Flux.fromStream(prosjekt.getFullmakt()
                .stream()
                .map(Link::getHref)
                .map(l -> fullmaktService.getFullmaktResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

}

