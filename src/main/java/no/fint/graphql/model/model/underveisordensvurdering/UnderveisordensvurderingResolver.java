
package no.fint.graphql.model.model.underveisordensvurdering;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.model.elevvurdering.ElevvurderingService;
import no.fint.graphql.model.model.karakterverdi.KarakterverdiService;
import no.fint.graphql.model.model.skolear.SkolearService;


import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.utdanning.vurdering.UnderveisordensvurderingResource;
import no.novari.fint.model.resource.utdanning.vurdering.ElevvurderingResource;
import no.novari.fint.model.resource.utdanning.vurdering.KarakterverdiResource;
import no.novari.fint.model.resource.utdanning.kodeverk.SkolearResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("modelUnderveisordensvurderingResolver")
public class UnderveisordensvurderingResolver implements GraphQLResolver<UnderveisordensvurderingResource> {

    @Autowired
    private ElevvurderingService elevvurderingService;

    @Autowired
    private KarakterverdiService karakterverdiService;

    @Autowired
    private SkolearService skolearService;


    public CompletionStage<ElevvurderingResource> getElevvurdering(UnderveisordensvurderingResource underveisordensvurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(underveisordensvurdering.getElevvurdering()
                .stream()
                .map(Link::getHref)
                .map(l -> elevvurderingService.getElevvurderingResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<KarakterverdiResource> getAtferd(UnderveisordensvurderingResource underveisordensvurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(underveisordensvurdering.getAtferd()
                .stream()
                .map(Link::getHref)
                .map(l -> karakterverdiService.getKarakterverdiResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<KarakterverdiResource> getOrden(UnderveisordensvurderingResource underveisordensvurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(underveisordensvurdering.getOrden()
                .stream()
                .map(Link::getHref)
                .map(l -> karakterverdiService.getKarakterverdiResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<SkolearResource> getSkolear(UnderveisordensvurderingResource underveisordensvurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(underveisordensvurdering.getSkolear()
                .stream()
                .map(Link::getHref)
                .map(l -> skolearService.getSkolearResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

}

