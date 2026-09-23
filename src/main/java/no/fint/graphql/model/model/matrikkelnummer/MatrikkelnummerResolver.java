
package no.fint.graphql.model.model.matrikkelnummer;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.kommune.KommuneService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.felles.kodeverk.KommuneResource;
import no.novari.fint.model.resource.felles.kompleksedatatyper.MatrikkelnummerResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelMatrikkelnummerResolver")
public class MatrikkelnummerResolver {

    @Autowired
    private KommuneService kommuneService;


    @SchemaMapping(typeName = "Matrikkelnummer", field = "kommunenummer")
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

