
package no.fint.graphql.model.model.stillingskode;

import graphql.schema.DataFetchingEnvironment;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.administrasjon.kodeverk.StillingskodeResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelStillingskodeResolver")
public class StillingskodeResolver {

    @Autowired
    private StillingskodeService stillingskodeService;


    @SchemaMapping(typeName = "Stillingskode", field = "forelder")
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

