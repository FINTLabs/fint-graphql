
package no.fint.graphql.model.utdanning.otenhet;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.felles.kommune.KommuneService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.kodeverk.OtEnhetResource;
import no.fint.model.resource.felles.kodeverk.KommuneResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("utdanningOtEnhetResolver")
public class OtEnhetResolver implements GraphQLResolver<OtEnhetResource> {

    @Autowired
    private KommuneService kommuneService;


    public CompletionStage<KommuneResource> getKommune(OtEnhetResource otenhet, DataFetchingEnvironment dfe) {
        return Flux.fromStream(otenhet.getKommune()
                .stream()
                .map(Link::getHref)
                .map(l -> kommuneService.getKommuneResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

}

