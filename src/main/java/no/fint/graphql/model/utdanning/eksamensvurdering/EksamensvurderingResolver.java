
package no.fint.graphql.model.utdanning.eksamensvurdering;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.utdanning.eksamensgruppe.EksamensgruppeService;
import no.fint.graphql.model.utdanning.karakterhistorie.KarakterhistorieService;
import no.fint.graphql.model.utdanning.elevvurdering.ElevvurderingService;
import no.fint.graphql.model.utdanning.fag.FagService;
import no.fint.graphql.model.utdanning.undervisningsgruppe.UndervisningsgruppeService;
import no.fint.graphql.model.utdanning.skolear.SkolearService;
import no.fint.graphql.model.utdanning.karakterverdi.KarakterverdiService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.vurdering.EksamensvurderingResource;
import no.fint.model.resource.utdanning.vurdering.EksamensgruppeResource;
import no.fint.model.resource.utdanning.vurdering.KarakterhistorieResource;
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

@Component("utdanningEksamensvurderingResolver")
public class EksamensvurderingResolver implements GraphQLResolver<EksamensvurderingResource> {

    @Autowired
    private EksamensgruppeService eksamensgruppeService;

    @Autowired
    private KarakterhistorieService karakterhistorieService;

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


    public CompletionStage<EksamensgruppeResource> getEksamensgruppe(EksamensvurderingResource eksamensvurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(eksamensvurdering.getEksamensgruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> eksamensgruppeService.getEksamensgruppeResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<KarakterhistorieResource> getKarakterhistorie(EksamensvurderingResource eksamensvurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(eksamensvurdering.getKarakterhistorie()
                .stream()
                .map(Link::getHref)
                .map(l -> karakterhistorieService.getKarakterhistorieResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<ElevvurderingResource> getElevvurdering(EksamensvurderingResource eksamensvurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(eksamensvurdering.getElevvurdering()
                .stream()
                .map(Link::getHref)
                .map(l -> elevvurderingService.getElevvurderingResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<FagResource> getFag(EksamensvurderingResource eksamensvurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(eksamensvurdering.getFag()
                .stream()
                .map(Link::getHref)
                .map(l -> fagService.getFagResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<UndervisningsgruppeResource> getUndervisningsgruppe(EksamensvurderingResource eksamensvurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(eksamensvurdering.getUndervisningsgruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> undervisningsgruppeService.getUndervisningsgruppeResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<SkolearResource> getSkolear(EksamensvurderingResource eksamensvurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(eksamensvurdering.getSkolear()
                .stream()
                .map(Link::getHref)
                .map(l -> skolearService.getSkolearResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<KarakterverdiResource> getKarakter(EksamensvurderingResource eksamensvurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(eksamensvurdering.getKarakter()
                .stream()
                .map(Link::getHref)
                .map(l -> karakterverdiService.getKarakterverdiResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

}

