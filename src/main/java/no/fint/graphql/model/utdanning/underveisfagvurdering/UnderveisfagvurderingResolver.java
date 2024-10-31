
package no.fint.graphql.model.utdanning.underveisfagvurdering;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.utdanning.elevforhold.ElevforholdService;
import no.fint.graphql.model.utdanning.elevvurdering.ElevvurderingService;
import no.fint.graphql.model.utdanning.fag.FagService;
import no.fint.graphql.model.utdanning.undervisningsgruppe.UndervisningsgruppeService;
import no.fint.graphql.model.utdanning.skolear.SkolearService;
import no.fint.graphql.model.utdanning.karakterverdi.KarakterverdiService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.vurdering.UnderveisfagvurderingResource;
import no.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.fint.model.resource.utdanning.vurdering.ElevvurderingResource;
import no.fint.model.resource.utdanning.timeplan.FagResource;
import no.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource;
import no.fint.model.resource.utdanning.kodeverk.SkolearResource;
import no.fint.model.resource.utdanning.vurdering.KarakterverdiResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("utdanningUnderveisfagvurderingResolver")
public class UnderveisfagvurderingResolver implements GraphQLResolver<UnderveisfagvurderingResource> {

    @Autowired
    private ElevforholdService elevforholdService;

    @Autowired
    private ElevvurderingService elevvurderingService;

    @Autowired
    private FagService fagService;

    @Autowired
    private UndervisningsgruppeService undervisningsgruppeService;

    @Autowired
    private SkolearService skolearService;

    @Autowired
    private KarakterverdiService karakterverdiService;


    public CompletionStage<ElevforholdResource> getElevforhold(UnderveisfagvurderingResource underveisfagvurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(underveisfagvurdering.getElevforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> elevforholdService.getElevforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<ElevvurderingResource> getElevvurdering(UnderveisfagvurderingResource underveisfagvurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(underveisfagvurdering.getElevvurdering()
                .stream()
                .map(Link::getHref)
                .map(l -> elevvurderingService.getElevvurderingResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<FagResource> getFag(UnderveisfagvurderingResource underveisfagvurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(underveisfagvurdering.getFag()
                .stream()
                .map(Link::getHref)
                .map(l -> fagService.getFagResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<UndervisningsgruppeResource> getUndervisningsgruppe(UnderveisfagvurderingResource underveisfagvurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(underveisfagvurdering.getUndervisningsgruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> undervisningsgruppeService.getUndervisningsgruppeResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<SkolearResource> getSkolear(UnderveisfagvurderingResource underveisfagvurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(underveisfagvurdering.getSkolear()
                .stream()
                .map(Link::getHref)
                .map(l -> skolearService.getSkolearResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

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

