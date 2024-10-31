
package no.fint.graphql.model.utdanning.elevvurdering;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.utdanning.elevforhold.ElevforholdService;
import no.fint.graphql.model.utdanning.sluttfagvurdering.SluttfagvurderingService;
import no.fint.graphql.model.utdanning.underveisordensvurdering.UnderveisordensvurderingService;
import no.fint.graphql.model.utdanning.vitnemalsmerknad.VitnemalsmerknadService;
import no.fint.graphql.model.utdanning.underveisfagvurdering.UnderveisfagvurderingService;
import no.fint.graphql.model.utdanning.halvarsordensvurdering.HalvarsordensvurderingService;
import no.fint.graphql.model.utdanning.halvarsfagvurdering.HalvarsfagvurderingService;
import no.fint.graphql.model.utdanning.sluttordensvurdering.SluttordensvurderingService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.vurdering.ElevvurderingResource;
import no.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.fint.model.resource.utdanning.vurdering.SluttfagvurderingResource;
import no.fint.model.resource.utdanning.vurdering.UnderveisordensvurderingResource;
import no.fint.model.resource.utdanning.kodeverk.VitnemalsmerknadResource;
import no.fint.model.resource.utdanning.vurdering.UnderveisfagvurderingResource;
import no.fint.model.resource.utdanning.vurdering.HalvarsordensvurderingResource;
import no.fint.model.resource.utdanning.vurdering.HalvarsfagvurderingResource;
import no.fint.model.resource.utdanning.vurdering.SluttordensvurderingResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("utdanningElevvurderingResolver")
public class ElevvurderingResolver implements GraphQLResolver<ElevvurderingResource> {

    @Autowired
    private ElevforholdService elevforholdService;

    @Autowired
    private SluttfagvurderingService sluttfagvurderingService;

    @Autowired
    private UnderveisordensvurderingService underveisordensvurderingService;

    @Autowired
    private VitnemalsmerknadService vitnemalsmerknadService;

    @Autowired
    private UnderveisfagvurderingService underveisfagvurderingService;

    @Autowired
    private HalvarsordensvurderingService halvarsordensvurderingService;

    @Autowired
    private HalvarsfagvurderingService halvarsfagvurderingService;

    @Autowired
    private SluttordensvurderingService sluttordensvurderingService;


    public CompletionStage<ElevforholdResource> getElevforhold(ElevvurderingResource elevvurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevvurdering.getElevforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> elevforholdService.getElevforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<List<SluttfagvurderingResource>> getSluttfagvurdering(ElevvurderingResource elevvurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevvurdering.getSluttfagvurdering()
                .stream()
                .map(Link::getHref)
                .map(l -> sluttfagvurderingService.getSluttfagvurderingResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<UnderveisordensvurderingResource>> getUnderveisordensvurdering(ElevvurderingResource elevvurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevvurdering.getUnderveisordensvurdering()
                .stream()
                .map(Link::getHref)
                .map(l -> underveisordensvurderingService.getUnderveisordensvurderingResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<VitnemalsmerknadResource>> getVitnemalsmerknad(ElevvurderingResource elevvurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevvurdering.getVitnemalsmerknad()
                .stream()
                .map(Link::getHref)
                .map(l -> vitnemalsmerknadService.getVitnemalsmerknadResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<UnderveisfagvurderingResource>> getUnderveisfagvurdering(ElevvurderingResource elevvurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevvurdering.getUnderveisfagvurdering()
                .stream()
                .map(Link::getHref)
                .map(l -> underveisfagvurderingService.getUnderveisfagvurderingResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<HalvarsordensvurderingResource>> getHalvarsordensvurdering(ElevvurderingResource elevvurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevvurdering.getHalvarsordensvurdering()
                .stream()
                .map(Link::getHref)
                .map(l -> halvarsordensvurderingService.getHalvarsordensvurderingResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<HalvarsfagvurderingResource>> getHalvarsfagvurdering(ElevvurderingResource elevvurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevvurdering.getHalvarsfagvurdering()
                .stream()
                .map(Link::getHref)
                .map(l -> halvarsfagvurderingService.getHalvarsfagvurderingResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<SluttordensvurderingResource>> getSluttordensvurdering(ElevvurderingResource elevvurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevvurdering.getSluttordensvurdering()
                .stream()
                .map(Link::getHref)
                .map(l -> sluttordensvurderingService.getSluttordensvurderingResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

}

