
package no.fint.graphql.model.model.eksamensvurdering;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.eksamensgruppe.EksamensgruppeService;
import no.fint.graphql.model.model.elevvurdering.ElevvurderingService;
import no.fint.graphql.model.model.fag.FagService;
import no.fint.graphql.model.model.karakterhistorie.KarakterhistorieService;
import no.fint.graphql.model.model.karakterverdi.KarakterverdiService;
import no.fint.graphql.model.model.skolear.SkolearService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.utdanning.kodeverk.SkolearResource;
import no.novari.fint.model.resource.utdanning.timeplan.FagResource;
import no.novari.fint.model.resource.utdanning.vurdering.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelEksamensvurderingResolver")
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

