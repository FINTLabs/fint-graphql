
package no.fint.graphql.model.model.sluttfagvurdering;

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
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Controller("modelSluttfagvurderingResolver")
public class SluttfagvurderingResolver {

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


    @SchemaMapping(typeName = "Sluttfagvurdering", field = "eksamensgruppe")
    public CompletionStage<EksamensgruppeResource> getEksamensgruppe(SluttfagvurderingResource sluttfagvurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(sluttfagvurdering.getEksamensgruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> eksamensgruppeService.getEksamensgruppeResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Sluttfagvurdering", field = "karakterhistorie")
    public CompletionStage<List<KarakterhistorieResource>> getKarakterhistorie(SluttfagvurderingResource sluttfagvurdering, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(sluttfagvurdering.getKarakterhistorie()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> karakterhistorieService.getKarakterhistorieResource(href, dfe)
                        .map(Optional::of)
                        .defaultIfEmpty(Optional.empty())
                        .onErrorResume(WebClientResponseException.class,
                                ex -> Mono.just(Optional.empty())),
                        8, 1)
                .collectList()
                .map(list -> list.stream()
                        .map(opt -> opt.orElse(null))
                        .collect(Collectors.toList()))
                .toFuture();
    }

    @SchemaMapping(typeName = "Sluttfagvurdering", field = "elevvurdering")
    public CompletionStage<ElevvurderingResource> getElevvurdering(SluttfagvurderingResource sluttfagvurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(sluttfagvurdering.getElevvurdering()
                .stream()
                .map(Link::getHref)
                .map(l -> elevvurderingService.getElevvurderingResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Sluttfagvurdering", field = "fag")
    public CompletionStage<FagResource> getFag(SluttfagvurderingResource sluttfagvurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(sluttfagvurdering.getFag()
                .stream()
                .map(Link::getHref)
                .map(l -> fagService.getFagResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Sluttfagvurdering", field = "skolear")
    public CompletionStage<SkolearResource> getSkolear(SluttfagvurderingResource sluttfagvurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(sluttfagvurdering.getSkolear()
                .stream()
                .map(Link::getHref)
                .map(l -> skolearService.getSkolearResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Sluttfagvurdering", field = "karakter")
    public CompletionStage<KarakterverdiResource> getKarakter(SluttfagvurderingResource sluttfagvurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(sluttfagvurdering.getKarakter()
                .stream()
                .map(Link::getHref)
                .map(l -> karakterverdiService.getKarakterverdiResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

}

