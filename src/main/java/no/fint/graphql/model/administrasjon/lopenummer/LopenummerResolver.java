
package no.fint.graphql.model.administrasjon.lopenummer;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.administrasjon.fullmakt.FullmaktService;


import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.kodeverk.LopenummerResource;
import no.fint.model.resource.administrasjon.fullmakt.FullmaktResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("administrasjonLopenummerResolver")
public class LopenummerResolver implements GraphQLResolver<LopenummerResource> {

    @Autowired
    private FullmaktService fullmaktService;


    public CompletionStage<List<FullmaktResource>> getFullmakt(LopenummerResource lopenummer, DataFetchingEnvironment dfe) {
        return Flux.fromStream(lopenummer.getFullmakt()
                .stream()
                .map(Link::getHref)
                .map(l -> fullmaktService.getFullmaktResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

}

