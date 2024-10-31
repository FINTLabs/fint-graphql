
package no.fint.graphql.model.administrasjon.funksjon;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.administrasjon.funksjon.FunksjonService;
import no.fint.graphql.model.administrasjon.fullmakt.FullmaktService;


import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.kodeverk.FunksjonResource;
import no.fint.model.resource.administrasjon.kodeverk.FunksjonResource;
import no.fint.model.resource.administrasjon.fullmakt.FullmaktResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("administrasjonFunksjonResolver")
public class FunksjonResolver implements GraphQLResolver<FunksjonResource> {

    @Autowired
    private FunksjonService funksjonService;

    @Autowired
    private FullmaktService fullmaktService;


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

    public CompletionStage<List<FullmaktResource>> getFullmakt(FunksjonResource funksjon, DataFetchingEnvironment dfe) {
        return Flux.fromStream(funksjon.getFullmakt()
                .stream()
                .map(Link::getHref)
                .map(l -> fullmaktService.getFullmaktResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

}

