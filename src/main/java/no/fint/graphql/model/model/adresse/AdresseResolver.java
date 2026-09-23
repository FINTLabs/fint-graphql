
package no.fint.graphql.model.model.adresse;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.landkode.LandkodeService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.felles.kodeverk.iso.LandkodeResource;
import no.novari.fint.model.resource.felles.kompleksedatatyper.AdresseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelAdresseResolver")
public class AdresseResolver {

    @Autowired
    private LandkodeService landkodeService;


    @SchemaMapping(typeName = "Adresse", field = "land")
    public CompletionStage<LandkodeResource> getLand(AdresseResource adresse, DataFetchingEnvironment dfe) {
        return Flux.fromStream(adresse.getLand()
                .stream()
                .map(Link::getHref)
                .map(l -> landkodeService.getLandkodeResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

}

