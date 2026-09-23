
package no.fint.graphql.model.model.karakterverdi;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.karakterskala.KarakterskalaService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.utdanning.kodeverk.KarakterskalaResource;
import no.novari.fint.model.resource.utdanning.vurdering.KarakterverdiResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelKarakterverdiResolver")
public class KarakterverdiResolver {

    @Autowired
    private KarakterskalaService karakterskalaService;


    @SchemaMapping(typeName = "Karakterverdi", field = "skala")
    public CompletionStage<KarakterskalaResource> getSkala(KarakterverdiResource karakterverdi, DataFetchingEnvironment dfe) {
        return Flux.fromStream(karakterverdi.getSkala()
                .stream()
                .map(Link::getHref)
                .map(l -> karakterskalaService.getKarakterskalaResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

}

