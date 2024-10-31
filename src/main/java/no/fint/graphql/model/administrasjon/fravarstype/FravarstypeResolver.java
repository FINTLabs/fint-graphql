
package no.fint.graphql.model.administrasjon.fravarstype;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.administrasjon.lonnsart.LonnsartService;


import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.kodeverk.FravarstypeResource;
import no.fint.model.resource.administrasjon.kodeverk.LonnsartResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("administrasjonFravarstypeResolver")
public class FravarstypeResolver implements GraphQLResolver<FravarstypeResource> {

    @Autowired
    private LonnsartService lonnsartService;


    public CompletionStage<LonnsartResource> getLonnsart(FravarstypeResource fravarstype, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fravarstype.getLonnsart()
                .stream()
                .map(Link::getHref)
                .map(l -> lonnsartService.getLonnsartResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

}

