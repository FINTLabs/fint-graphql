
package no.fint.graphql.model.utdanning.halvarsordensvurdering;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.utdanning.elevforhold.ElevforholdService;
import no.fint.graphql.model.utdanning.elevvurdering.ElevvurderingService;
import no.fint.graphql.model.utdanning.karakterverdi.KarakterverdiService;
import no.fint.graphql.model.utdanning.skolear.SkolearService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.vurdering.HalvarsordensvurderingResource;
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

@Component("utdanningHalvarsordensvurderingResolver")
public class HalvarsordensvurderingResolver implements GraphQLResolver<HalvarsordensvurderingResource> {

    @Autowired
    private ElevforholdService elevforholdService;

    @Autowired
    private ElevvurderingService elevvurderingService;

    @Autowired
    private KarakterverdiService karakterverdiService;

    @Autowired
    private SkolearService skolearService;


    public CompletionStage<ElevforholdResource> getElevforhold(HalvarsordensvurderingResource halvarsordensvurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(halvarsordensvurdering.getElevforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> elevforholdService.getElevforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<ElevvurderingResource> getElevvurdering(HalvarsordensvurderingResource halvarsordensvurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(halvarsordensvurdering.getElevvurdering()
                .stream()
                .map(Link::getHref)
                .map(l -> elevvurderingService.getElevvurderingResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<KarakterverdiResource> getAtferd(HalvarsordensvurderingResource halvarsordensvurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(halvarsordensvurdering.getAtferd()
                .stream()
                .map(Link::getHref)
                .map(l -> karakterverdiService.getKarakterverdiResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<KarakterverdiResource> getOrden(HalvarsordensvurderingResource halvarsordensvurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(halvarsordensvurdering.getOrden()
                .stream()
                .map(Link::getHref)
                .map(l -> karakterverdiService.getKarakterverdiResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<SkolearResource> getSkolear(HalvarsordensvurderingResource halvarsordensvurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(halvarsordensvurdering.getSkolear()
                .stream()
                .map(Link::getHref)
                .map(l -> skolearService.getSkolearResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

}

