
package no.fint.graphql.model.utdanning.faggruppemedlemskap;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.utdanning.fagmerknad.FagmerknadService;
import no.fint.graphql.model.utdanning.fagstatus.FagstatusService;
import no.fint.graphql.model.utdanning.elevforhold.ElevforholdService;
import no.fint.graphql.model.utdanning.varsel.VarselService;
import no.fint.graphql.model.utdanning.faggruppe.FaggruppeService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.timeplan.FaggruppemedlemskapResource;
import no.fint.model.resource.utdanning.kodeverk.FagmerknadResource;
import no.fint.model.resource.utdanning.kodeverk.FagstatusResource;
import no.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.fint.model.resource.utdanning.elev.VarselResource;
import no.fint.model.resource.utdanning.timeplan.FaggruppeResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("utdanningFaggruppemedlemskapResolver")
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
        return Flux.fromStream(faggruppemedlemskap.getVarsel()
                .stream()
                .map(Link::getHref)
                .map(l -> varselService.getVarselResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
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

