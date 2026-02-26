
package no.fint.graphql.model.model.matrikkelnummer;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.kommune.KommuneService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.felles.kodeverk.KommuneResource;
import no.novari.fint.model.resource.felles.kompleksedatatyper.MatrikkelnummerResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelMatrikkelnummerResolver")
public class MatrikkelnummerResolver implements GraphQLResolver<MatrikkelnummerResource> {

    @Autowired
    private KommuneService kommuneService;


    public CompletionStage<KommuneResource> getKommunenummer(MatrikkelnummerResource matrikkelnummer, DataFetchingEnvironment dfe) {
        return Flux.fromStream(matrikkelnummer.getKommunenummer()
                .stream()
                .map(Link::getHref)
                .map(l -> kommuneService.getKommuneResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

}

