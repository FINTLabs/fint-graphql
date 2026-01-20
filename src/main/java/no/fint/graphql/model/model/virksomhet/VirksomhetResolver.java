
package no.fint.graphql.model.model.virksomhet;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.model.larling.LarlingService;


import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.felles.VirksomhetResource;
import no.novari.fint.model.resource.utdanning.larling.LarlingResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("modelVirksomhetResolver")
public class VirksomhetResolver implements GraphQLResolver<VirksomhetResource> {

    @Autowired
    private LarlingService larlingService;


    public CompletionStage<List<LarlingResource>> getLarling(VirksomhetResource virksomhet, DataFetchingEnvironment dfe) {
        return Flux.fromStream(virksomhet.getLarling()
                .stream()
                .map(Link::getHref)
                .map(l -> larlingService.getLarlingResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

}

