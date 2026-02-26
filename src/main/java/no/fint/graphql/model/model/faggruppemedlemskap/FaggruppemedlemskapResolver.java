
package no.fint.graphql.model.model.faggruppemedlemskap;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.elevforhold.ElevforholdService;
import no.fint.graphql.model.model.faggruppe.FaggruppeService;
import no.fint.graphql.model.model.fagmerknad.FagmerknadService;
import no.fint.graphql.model.model.fagstatus.FagstatusService;
import no.fint.graphql.model.model.varsel.VarselService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.novari.fint.model.resource.utdanning.elev.VarselResource;
import no.novari.fint.model.resource.utdanning.kodeverk.FagmerknadResource;
import no.novari.fint.model.resource.utdanning.kodeverk.FagstatusResource;
import no.novari.fint.model.resource.utdanning.timeplan.FaggruppeResource;
import no.novari.fint.model.resource.utdanning.timeplan.FaggruppemedlemskapResource;
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

@Component("modelFaggruppemedlemskapResolver")
public class FaggruppemedlemskapResolver implements GraphQLResolver<FaggruppemedlemskapResource> {

    @Autowired
    private FagmerknadService fagmerknadService;

    @Autowired
    private FagstatusService fagstatusService;

    @Autowired
    private ElevforholdService elevforholdService;

    @Autowired
    private VarselService varselService;

    @Autowired
    private FaggruppeService faggruppeService;


    public CompletionStage<FagmerknadResource> getFagmerknad(FaggruppemedlemskapResource faggruppemedlemskap, DataFetchingEnvironment dfe) {
        return Flux.fromStream(faggruppemedlemskap.getFagmerknad()
                .stream()
                .map(Link::getHref)
                .map(l -> fagmerknadService.getFagmerknadResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<FagstatusResource> getFagstatus(FaggruppemedlemskapResource faggruppemedlemskap, DataFetchingEnvironment dfe) {
        return Flux.fromStream(faggruppemedlemskap.getFagstatus()
                .stream()
                .map(Link::getHref)
                .map(l -> fagstatusService.getFagstatusResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<ElevforholdResource> getElevforhold(FaggruppemedlemskapResource faggruppemedlemskap, DataFetchingEnvironment dfe) {
        return Flux.fromStream(faggruppemedlemskap.getElevforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> elevforholdService.getElevforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<List<VarselResource>> getVarsel(FaggruppemedlemskapResource faggruppemedlemskap, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(faggruppemedlemskap.getVarsel()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> varselService.getVarselResource(href, dfe)
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

    public CompletionStage<FaggruppeResource> getFaggruppe(FaggruppemedlemskapResource faggruppemedlemskap, DataFetchingEnvironment dfe) {
        return Flux.fromStream(faggruppemedlemskap.getFaggruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> faggruppeService.getFaggruppeResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

}

