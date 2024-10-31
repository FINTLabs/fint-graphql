
package no.fint.graphql.model.utdanning.underveisordensvurdering;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.utdanning.elevforhold.ElevforholdService;
import no.fint.graphql.model.utdanning.elevvurdering.ElevvurderingService;
import no.fint.graphql.model.utdanning.karakterverdi.KarakterverdiService;
import no.fint.graphql.model.utdanning.skolear.SkolearService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.vurdering.UnderveisordensvurderingResource;
import no.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.fint.model.resource.utdanning.vurdering.ElevvurderingResource;
import no.fint.model.resource.utdanning.vurdering.KarakterverdiResource;
import no.fint.model.resource.utdanning.kodeverk.SkolearResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("utdanningUnderveisordensvurderingResolver")
public class UnderveisordensvurderingResolver implements GraphQLResolver<UnderveisordensvurderingResource> {

    @Autowired
    private ElevforholdService elevforholdService;

    @Autowired
    private ElevvurderingService elevvurderingService;

    @Autowired
    private KarakterverdiService karakterverdiService;

    @Autowired
    private SkolearService skolearService;


    public CompletionStage<ElevforholdResource> getElevforhold(UnderveisordensvurderingResource underveisordensvurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(underveisordensvurdering.getElevforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> elevforholdService.getElevforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

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

