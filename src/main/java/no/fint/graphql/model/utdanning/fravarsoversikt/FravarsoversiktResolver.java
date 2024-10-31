
package no.fint.graphql.model.utdanning.fravarsoversikt;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.utdanning.elevforhold.ElevforholdService;
import no.fint.graphql.model.utdanning.fag.FagService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.vurdering.FravarsoversiktResource;
import no.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.fint.model.resource.utdanning.timeplan.FagResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("utdanningFravarsoversiktResolver")
public class FravarsoversiktResolver implements GraphQLResolver<FravarsoversiktResource> {

    @Autowired
    private ElevforholdService elevforholdService;

    @Autowired
    private FagService fagService;


    public CompletionStage<ElevforholdResource> getElevforhold(FravarsoversiktResource fravarsoversikt, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fravarsoversikt.getElevforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> elevforholdService.getElevforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<FagResource> getFag(FravarsoversiktResource fravarsoversikt, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fravarsoversikt.getFag()
                .stream()
                .map(Link::getHref)
                .map(l -> fagService.getFagResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

}

