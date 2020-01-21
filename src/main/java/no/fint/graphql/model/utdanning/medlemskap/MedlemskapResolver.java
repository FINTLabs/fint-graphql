
package no.fint.graphql.model.utdanning.medlemskap;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.utdanning.vurdering.VurderingService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.elev.MedlemskapResource;
import no.fint.model.resource.utdanning.vurdering.VurderingResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("utdanningMedlemskapResolver")
public class MedlemskapResolver implements GraphQLResolver<MedlemskapResource> {

    @Autowired
    private VurderingService vurderingService;


    public CompletionStage<List<VurderingResource>> getFortlopendeVurdering(MedlemskapResource medlemskap, DataFetchingEnvironment dfe) {
        return Flux.fromStream(medlemskap.getFortlopendeVurdering()
                .stream()
                .map(Link::getHref)
                .map(l -> vurderingService.getVurderingResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<VurderingResource> getEndeligVurdering(MedlemskapResource medlemskap, DataFetchingEnvironment dfe) {
        return Flux.fromStream(medlemskap.getEndeligVurdering()
                .stream()
                .map(Link::getHref)
                .map(l -> vurderingService.getVurderingResource(l, dfe)))
                .flatMap(Mono::flux)
                .singleOrEmpty()
                .toFuture();
    }

}

