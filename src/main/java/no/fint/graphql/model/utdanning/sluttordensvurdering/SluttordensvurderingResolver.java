
package no.fint.graphql.model.utdanning.sluttordensvurdering;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.utdanning.elevforhold.ElevforholdService;
import no.fint.graphql.model.utdanning.elevvurdering.ElevvurderingService;
import no.fint.graphql.model.utdanning.karakterverdi.KarakterverdiService;
import no.fint.graphql.model.utdanning.skolear.SkolearService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.vurdering.SluttordensvurderingResource;
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

@Component("utdanningSluttordensvurderingResolver")
public class SluttordensvurderingResolver implements GraphQLResolver<SluttordensvurderingResource> {

    @Autowired
    private ElevforholdService elevforholdService;

    @Autowired
    private ElevvurderingService elevvurderingService;

    @Autowired
    private KarakterverdiService karakterverdiService;

    @Autowired
    private SkolearService skolearService;


    public CompletionStage<ElevforholdResource> getElevforhold(SluttordensvurderingResource sluttordensvurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(sluttordensvurdering.getElevforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> elevforholdService.getElevforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<ElevvurderingResource> getElevvurdering(SluttordensvurderingResource sluttordensvurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(sluttordensvurdering.getElevvurdering()
                .stream()
                .map(Link::getHref)
                .map(l -> elevvurderingService.getElevvurderingResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<KarakterverdiResource> getAtferd(SluttordensvurderingResource sluttordensvurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(sluttordensvurdering.getAtferd()
                .stream()
                .map(Link::getHref)
                .map(l -> karakterverdiService.getKarakterverdiResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<KarakterverdiResource> getOrden(SluttordensvurderingResource sluttordensvurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(sluttordensvurdering.getOrden()
                .stream()
                .map(Link::getHref)
                .map(l -> karakterverdiService.getKarakterverdiResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<SkolearResource> getSkolear(SluttordensvurderingResource sluttordensvurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(sluttordensvurdering.getSkolear()
                .stream()
                .map(Link::getHref)
                .map(l -> skolearService.getSkolearResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

}

