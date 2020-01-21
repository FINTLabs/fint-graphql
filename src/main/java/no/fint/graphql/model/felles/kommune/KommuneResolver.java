
package no.fint.graphql.model.felles.kommune;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.felles.fylke.FylkeService;


import no.fint.model.resource.Link;
import no.fint.model.resource.felles.kodeverk.KommuneResource;
import no.fint.model.resource.felles.kodeverk.FylkeResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("fellesKommuneResolver")
public class KommuneResolver implements GraphQLResolver<KommuneResource> {

    @Autowired
    private FylkeService fylkeService;


    public CompletionStage<FylkeResource> getFylke(KommuneResource kommune, DataFetchingEnvironment dfe) {
        return Flux.fromStream(kommune.getFylke()
                .stream()
                .map(Link::getHref)
                .map(l -> fylkeService.getFylkeResource(l, dfe)))
                .flatMap(Mono::flux)
                .singleOrEmpty()
                .toFuture();
    }

}

