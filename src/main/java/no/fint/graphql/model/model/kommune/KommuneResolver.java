
package no.fint.graphql.model.model.kommune;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.fylke.FylkeService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.felles.kodeverk.FylkeResource;
import no.novari.fint.model.resource.felles.kodeverk.KommuneResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelKommuneResolver")
public class KommuneResolver implements GraphQLResolver<KommuneResource> {

    @Autowired
    private FylkeService fylkeService;


    public CompletionStage<FylkeResource> getFylke(KommuneResource kommune, DataFetchingEnvironment dfe) {
        return Flux.fromStream(kommune.getFylke()
                .stream()
                .map(Link::getHref)
                .map(l -> fylkeService.getFylkeResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

}

