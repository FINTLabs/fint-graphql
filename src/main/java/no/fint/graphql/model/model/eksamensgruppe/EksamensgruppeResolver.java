
package no.fint.graphql.model.model.eksamensgruppe;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.eksamen.EksamenService;
import no.fint.graphql.model.model.eksamensform.EksamensformService;
import no.fint.graphql.model.model.eksamensgruppemedlemskap.EksamensgruppemedlemskapService;
import no.fint.graphql.model.model.fag.FagService;
import no.fint.graphql.model.model.sensor.SensorService;
import no.fint.graphql.model.model.skole.SkoleService;
import no.fint.graphql.model.model.skolear.SkolearService;
import no.fint.graphql.model.model.termin.TerminService;
import no.fint.graphql.model.model.undervisningsforhold.UndervisningsforholdService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.utdanning.elev.UndervisningsforholdResource;
import no.novari.fint.model.resource.utdanning.kodeverk.EksamensformResource;
import no.novari.fint.model.resource.utdanning.kodeverk.SkolearResource;
import no.novari.fint.model.resource.utdanning.kodeverk.TerminResource;
import no.novari.fint.model.resource.utdanning.timeplan.EksamenResource;
import no.novari.fint.model.resource.utdanning.timeplan.FagResource;
import no.novari.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.novari.fint.model.resource.utdanning.vurdering.EksamensgruppeResource;
import no.novari.fint.model.resource.utdanning.vurdering.EksamensgruppemedlemskapResource;
import no.novari.fint.model.resource.utdanning.vurdering.SensorResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Component("modelEksamensgruppeResolver")
public class EksamensgruppeResolver implements GraphQLResolver<EksamensgruppeResource> {

    @Autowired
    private UndervisningsforholdService undervisningsforholdService;

    @Autowired
    private EksamenService eksamenService;

    @Autowired
    private FagService fagService;

    @Autowired
    private SkoleService skoleService;

    @Autowired
    private TerminService terminService;

    @Autowired
    private EksamensformService eksamensformService;

    @Autowired
    private SkolearService skolearService;

    @Autowired
    private EksamensgruppemedlemskapService eksamensgruppemedlemskapService;

    @Autowired
    private SensorService sensorService;


    public CompletionStage<List<UndervisningsforholdResource>> getUndervisningsforhold(EksamensgruppeResource eksamensgruppe, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(eksamensgruppe.getUndervisningsforhold()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> undervisningsforholdService.getUndervisningsforholdResource(href, dfe)
                        .map(Optional::of)
                        .onErrorResume(WebClientResponseException.class,
                                ex -> Mono.just(Optional.empty())),
                        8, 1)
                .collectList()
                .map(list -> list.stream()
                        .map(opt -> opt.orElse(null))
                        .collect(Collectors.toList()))
                .toFuture();
    }

    public CompletionStage<EksamenResource> getEksamen(EksamensgruppeResource eksamensgruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(eksamensgruppe.getEksamen()
                .stream()
                .map(Link::getHref)
                .map(l -> eksamenService.getEksamenResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<FagResource> getFag(EksamensgruppeResource eksamensgruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(eksamensgruppe.getFag()
                .stream()
                .map(Link::getHref)
                .map(l -> fagService.getFagResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<SkoleResource> getSkole(EksamensgruppeResource eksamensgruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(eksamensgruppe.getSkole()
                .stream()
                .map(Link::getHref)
                .map(l -> skoleService.getSkoleResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<TerminResource> getTermin(EksamensgruppeResource eksamensgruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(eksamensgruppe.getTermin()
                .stream()
                .map(Link::getHref)
                .map(l -> terminService.getTerminResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<EksamensformResource> getEksamensform(EksamensgruppeResource eksamensgruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(eksamensgruppe.getEksamensform()
                .stream()
                .map(Link::getHref)
                .map(l -> eksamensformService.getEksamensformResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<SkolearResource> getSkolear(EksamensgruppeResource eksamensgruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(eksamensgruppe.getSkolear()
                .stream()
                .map(Link::getHref)
                .map(l -> skolearService.getSkolearResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<List<EksamensgruppemedlemskapResource>> getGruppemedlemskap(EksamensgruppeResource eksamensgruppe, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(eksamensgruppe.getGruppemedlemskap()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> eksamensgruppemedlemskapService.getEksamensgruppemedlemskapResource(href, dfe)
                        .map(Optional::of)
                        .onErrorResume(WebClientResponseException.class,
                                ex -> Mono.just(Optional.empty())),
                        8, 1)
                .collectList()
                .map(list -> list.stream()
                        .map(opt -> opt.orElse(null))
                        .collect(Collectors.toList()))
                .toFuture();
    }

    public CompletionStage<List<SensorResource>> getSensor(EksamensgruppeResource eksamensgruppe, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(eksamensgruppe.getSensor()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> sensorService.getSensorResource(href, dfe)
                        .map(Optional::of)
                        .onErrorResume(WebClientResponseException.class,
                                ex -> Mono.just(Optional.empty())),
                        8, 1)
                .collectList()
                .map(list -> list.stream()
                        .map(opt -> opt.orElse(null))
                        .collect(Collectors.toList()))
                .toFuture();
    }

}

