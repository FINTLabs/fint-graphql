
package no.fint.graphql.model.utdanning.karakterverdi;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.utdanning.karakterskala.KarakterskalaService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.vurdering.KarakterverdiResource;
import no.fint.model.resource.utdanning.kodeverk.KarakterskalaResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("utdanningKarakterverdiResolver")
public class KarakterverdiResolver implements GraphQLResolver<KarakterverdiResource> {

    @Autowired
    private KarakterskalaService karakterskalaService;


    public CompletionStage<KarakterskalaResource> getSkala(KarakterverdiResource karakterverdi, DataFetchingEnvironment dfe) {
        return Flux.fromStream(karakterverdi.getSkala()
                .stream()
                .map(Link::getHref)
                .map(l -> karakterskalaService.getKarakterskalaResource(l, dfe)))
                .flatMap(Mono::flux)
                .singleOrEmpty()
                .toFuture();
    }

}

