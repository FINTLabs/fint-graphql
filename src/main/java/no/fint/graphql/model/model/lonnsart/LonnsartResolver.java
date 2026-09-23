
package no.fint.graphql.model.model.lonnsart;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.art.ArtService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.administrasjon.kodeverk.ArtResource;
import no.novari.fint.model.resource.administrasjon.kodeverk.LonnsartResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelLonnsartResolver")
public class LonnsartResolver {

    @Autowired
    private ArtService artService;


    @SchemaMapping(typeName = "Lonnsart", field = "art")
    public CompletionStage<ArtResource> getArt(LonnsartResource lonnsart, DataFetchingEnvironment dfe) {
        return Flux.fromStream(lonnsart.getArt()
                .stream()
                .map(Link::getHref)
                .map(l -> artService.getArtResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

}

