
package no.fint.graphql.model.model.otenhet;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.kommune.KommuneService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.felles.kodeverk.KommuneResource;
import no.novari.fint.model.resource.utdanning.kodeverk.OtEnhetResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelOtEnhetResolver")
public class OtEnhetResolver {

    @Autowired
    private KommuneService kommuneService;


    @SchemaMapping(typeName = "OtEnhet", field = "kommune")
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

