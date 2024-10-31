
package no.fint.graphql.model.felles.matrikkelnummer;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.felles.kommune.KommuneService;


import no.fint.model.resource.Link;
import no.fint.model.resource.felles.kompleksedatatyper.MatrikkelnummerResource;
import no.fint.model.resource.felles.kodeverk.KommuneResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("fellesMatrikkelnummerResolver")
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

