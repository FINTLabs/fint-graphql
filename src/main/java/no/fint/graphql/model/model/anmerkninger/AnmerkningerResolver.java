
package no.fint.graphql.model.model.anmerkninger;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.skolear.SkolearService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.utdanning.kodeverk.SkolearResource;
import no.novari.fint.model.resource.utdanning.vurdering.AnmerkningerResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelAnmerkningerResolver")
public class AnmerkningerResolver implements GraphQLResolver<AnmerkningerResource> {

    @Autowired
    private SkolearService skolearService;


    public CompletionStage<SkolearResource> getSkolear(AnmerkningerResource anmerkninger, DataFetchingEnvironment dfe) {
        return Flux.fromStream(anmerkninger.getSkolear()
                .stream()
                .map(Link::getHref)
                .map(l -> skolearService.getSkolearResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

}

