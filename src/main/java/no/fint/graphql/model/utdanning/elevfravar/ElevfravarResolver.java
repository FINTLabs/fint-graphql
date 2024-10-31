
package no.fint.graphql.model.utdanning.elevfravar;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.utdanning.fravarsregistrering.FravarsregistreringService;
import no.fint.graphql.model.utdanning.elevforhold.ElevforholdService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.vurdering.ElevfravarResource;
import no.fint.model.resource.utdanning.vurdering.FravarsregistreringResource;
import no.fint.model.resource.utdanning.elev.ElevforholdResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("utdanningElevfravarResolver")
public class ElevfravarResolver implements GraphQLResolver<ElevfravarResource> {

    @Autowired
    private FravarsregistreringService fravarsregistreringService;

    @Autowired
    private ElevforholdService elevforholdService;


    public CompletionStage<List<FravarsregistreringResource>> getFravarsregistrering(ElevfravarResource elevfravar, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevfravar.getFravarsregistrering()
                .stream()
                .map(Link::getHref)
                .map(l -> fravarsregistreringService.getFravarsregistreringResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<ElevforholdResource> getElevforhold(ElevfravarResource elevfravar, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevfravar.getElevforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> elevforholdService.getElevforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

}

