
package no.fint.graphql.model.model.adresse;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.landkode.LandkodeService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.felles.kodeverk.iso.LandkodeResource;
import no.novari.fint.model.resource.felles.kompleksedatatyper.AdresseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelAdresseResolver")
public class AdresseResolver implements GraphQLResolver<AdresseResource> {

    @Autowired
    private LandkodeService landkodeService;


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

