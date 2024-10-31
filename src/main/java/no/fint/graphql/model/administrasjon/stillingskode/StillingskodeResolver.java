
package no.fint.graphql.model.administrasjon.stillingskode;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.administrasjon.stillingskode.StillingskodeService;


import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.kodeverk.StillingskodeResource;
import no.fint.model.resource.administrasjon.kodeverk.StillingskodeResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("administrasjonStillingskodeResolver")
public class StillingskodeResolver implements GraphQLResolver<StillingskodeResource> {

    @Autowired
    private StillingskodeService stillingskodeService;


    public CompletionStage<StillingskodeResource> getForelder(StillingskodeResource stillingskode, DataFetchingEnvironment dfe) {
        return Flux.fromStream(stillingskode.getForelder()
                .stream()
                .map(Link::getHref)
                .map(l -> stillingskodeService.getStillingskodeResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

}

