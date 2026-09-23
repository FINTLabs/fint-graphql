
package no.fint.graphql.model.model.kommune;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.fylke.FylkeService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.felles.kodeverk.FylkeResource;
import no.novari.fint.model.resource.felles.kodeverk.KommuneResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelKommuneResolver")
public class KommuneResolver {

    @Autowired
    private FylkeService fylkeService;


    @SchemaMapping(typeName = "Kommune", field = "fylke")
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

