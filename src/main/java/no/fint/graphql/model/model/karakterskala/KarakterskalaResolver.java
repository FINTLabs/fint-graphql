
package no.fint.graphql.model.model.karakterskala;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.model.karakterverdi.KarakterverdiService;


import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.utdanning.kodeverk.KarakterskalaResource;
import no.novari.fint.model.resource.utdanning.vurdering.KarakterverdiResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("modelKarakterskalaResolver")
public class KarakterskalaResolver implements GraphQLResolver<KarakterskalaResource> {

    @Autowired
    private KarakterverdiService karakterverdiService;


    public CompletionStage<List<KarakterverdiResource>> getVerdi(KarakterskalaResource karakterskala, DataFetchingEnvironment dfe) {
        return Flux.fromStream(karakterskala.getVerdi()
                .stream()
                .map(Link::getHref)
                .map(l -> karakterverdiService.getKarakterverdiResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

}

