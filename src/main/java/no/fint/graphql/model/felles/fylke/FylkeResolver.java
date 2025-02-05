
package no.fint.graphql.model.felles.fylke;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.felles.kommune.KommuneService;


import no.fint.model.resource.Link;
import no.fint.model.resource.felles.kodeverk.FylkeResource;
import no.fint.model.resource.felles.kodeverk.KommuneResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("fellesFylkeResolver")
public class FylkeResolver implements GraphQLResolver<FylkeResource> {

    @Autowired
    private KommuneService kommuneService;


    public CompletionStage<List<KommuneResource>> getKommune(FylkeResource fylke, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fylke.getKommune()
                .stream()
                .map(Link::getHref)
                .map(l -> kommuneService.getKommuneResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

}

