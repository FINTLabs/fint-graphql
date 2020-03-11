
package no.fint.graphql.model.administrasjon.prosjekt;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.administrasjon.fullmakt.FullmaktService;


import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.kodeverk.ProsjektResource;
import no.fint.model.resource.administrasjon.fullmakt.FullmaktResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("administrasjonProsjektResolver")
public class ProsjektResolver implements GraphQLResolver<ProsjektResource> {

    @Autowired
    private FullmaktService fullmaktService;


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

