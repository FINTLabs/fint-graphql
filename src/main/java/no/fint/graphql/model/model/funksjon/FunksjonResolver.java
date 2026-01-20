
package no.fint.graphql.model.model.funksjon;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.model.funksjon.FunksjonService;


import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.administrasjon.kodeverk.FunksjonResource;
import no.novari.fint.model.resource.administrasjon.kodeverk.FunksjonResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("modelFunksjonResolver")
public class FunksjonResolver implements GraphQLResolver<FunksjonResource> {

    @Autowired
    private FunksjonService funksjonService;


    public CompletionStage<FunksjonResource> getOverordnet(FunksjonResource funksjon, DataFetchingEnvironment dfe) {
        return Flux.fromStream(funksjon.getOverordnet()
                .stream()
                .map(Link::getHref)
                .map(l -> funksjonService.getFunksjonResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<List<FunksjonResource>> getUnderordnet(FunksjonResource funksjon, DataFetchingEnvironment dfe) {
        return Flux.fromStream(funksjon.getUnderordnet()
                .stream()
                .map(Link::getHref)
                .map(l -> funksjonService.getFunksjonResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

}

