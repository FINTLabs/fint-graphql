
package no.fint.graphql.model.model.underveisfagvurdering;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.elevvurdering.ElevvurderingService;
import no.fint.graphql.model.model.fag.FagService;
import no.fint.graphql.model.model.karakterverdi.KarakterverdiService;
import no.fint.graphql.model.model.skolear.SkolearService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.utdanning.kodeverk.SkolearResource;
import no.novari.fint.model.resource.utdanning.timeplan.FagResource;
import no.novari.fint.model.resource.utdanning.vurdering.ElevvurderingResource;
import no.novari.fint.model.resource.utdanning.vurdering.KarakterverdiResource;
import no.novari.fint.model.resource.utdanning.vurdering.UnderveisfagvurderingResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelUnderveisfagvurderingResolver")
public class UnderveisfagvurderingResolver {

    @Autowired
    private ElevvurderingService elevvurderingService;

    @Autowired
    private FagService fagService;

    @Autowired
    private SkolearService skolearService;

    @Autowired
    private KarakterverdiService karakterverdiService;


    @SchemaMapping(typeName = "Underveisfagvurdering", field = "elevvurdering")
    public CompletionStage<ElevvurderingResource> getElevvurdering(UnderveisfagvurderingResource underveisfagvurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(underveisfagvurdering.getElevvurdering()
                .stream()
                .map(Link::getHref)
                .map(l -> elevvurderingService.getElevvurderingResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Underveisfagvurdering", field = "fag")
    public CompletionStage<FagResource> getFag(UnderveisfagvurderingResource underveisfagvurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(underveisfagvurdering.getFag()
                .stream()
                .map(Link::getHref)
                .map(l -> fagService.getFagResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Underveisfagvurdering", field = "skolear")
    public CompletionStage<SkolearResource> getSkolear(UnderveisfagvurderingResource underveisfagvurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(underveisfagvurdering.getSkolear()
                .stream()
                .map(Link::getHref)
                .map(l -> skolearService.getSkolearResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Underveisfagvurdering", field = "karakter")
    public CompletionStage<KarakterverdiResource> getKarakter(UnderveisfagvurderingResource underveisfagvurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(underveisfagvurdering.getKarakter()
                .stream()
                .map(Link::getHref)
                .map(l -> karakterverdiService.getKarakterverdiResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

}

