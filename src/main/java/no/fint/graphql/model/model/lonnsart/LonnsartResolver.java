
package no.fint.graphql.model.model.lonnsart;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.model.art.ArtService;


import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.administrasjon.kodeverk.LonnsartResource;
import no.novari.fint.model.resource.administrasjon.kodeverk.ArtResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("modelLonnsartResolver")
public class LonnsartResolver implements GraphQLResolver<LonnsartResource> {

    @Autowired
    private ArtService artService;


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

