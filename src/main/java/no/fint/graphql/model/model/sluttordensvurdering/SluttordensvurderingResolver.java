
package no.fint.graphql.model.model.sluttordensvurdering;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.elevvurdering.ElevvurderingService;
import no.fint.graphql.model.model.karakterverdi.KarakterverdiService;
import no.fint.graphql.model.model.skolear.SkolearService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.utdanning.kodeverk.SkolearResource;
import no.novari.fint.model.resource.utdanning.vurdering.ElevvurderingResource;
import no.novari.fint.model.resource.utdanning.vurdering.KarakterverdiResource;
import no.novari.fint.model.resource.utdanning.vurdering.SluttordensvurderingResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelSluttordensvurderingResolver")
public class SluttordensvurderingResolver {

    @Autowired
    private ElevvurderingService elevvurderingService;

    @Autowired
    private KarakterverdiService karakterverdiService;

    @Autowired
    private SkolearService skolearService;


    @SchemaMapping(typeName = "Sluttordensvurdering", field = "elevvurdering")
    public CompletionStage<ElevvurderingResource> getElevvurdering(SluttordensvurderingResource sluttordensvurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(sluttordensvurdering.getElevvurdering()
                .stream()
                .map(Link::getHref)
                .map(l -> elevvurderingService.getElevvurderingResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Sluttordensvurdering", field = "atferd")
    public CompletionStage<KarakterverdiResource> getAtferd(SluttordensvurderingResource sluttordensvurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(sluttordensvurdering.getAtferd()
                .stream()
                .map(Link::getHref)
                .map(l -> karakterverdiService.getKarakterverdiResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Sluttordensvurdering", field = "orden")
    public CompletionStage<KarakterverdiResource> getOrden(SluttordensvurderingResource sluttordensvurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(sluttordensvurdering.getOrden()
                .stream()
                .map(Link::getHref)
                .map(l -> karakterverdiService.getKarakterverdiResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Sluttordensvurdering", field = "skolear")
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

