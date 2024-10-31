
package no.fint.graphql.model.utdanning.faggruppe;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.utdanning.fag.FagService;
import no.fint.graphql.model.utdanning.skole.SkoleService;
import no.fint.graphql.model.utdanning.skolear.SkolearService;
import no.fint.graphql.model.utdanning.faggruppemedlemskap.FaggruppemedlemskapService;
import no.fint.graphql.model.utdanning.medlemskap.MedlemskapService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.timeplan.FaggruppeResource;
import no.fint.model.resource.utdanning.timeplan.FagResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.model.resource.utdanning.kodeverk.SkolearResource;
import no.fint.model.resource.utdanning.timeplan.FaggruppemedlemskapResource;
import no.fint.model.resource.utdanning.elev.MedlemskapResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("utdanningFaggruppeResolver")
public class FaggruppeResolver implements GraphQLResolver<FaggruppeResource> {

    @Autowired
    private FagService fagService;

    @Autowired
    private SkoleService skoleService;

    @Autowired
    private SkolearService skolearService;

    @Autowired
    private FaggruppemedlemskapService faggruppemedlemskapService;

    @Autowired
    private MedlemskapService medlemskapService;


    public CompletionStage<FagResource> getFag(FaggruppeResource faggruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(faggruppe.getFag()
                .stream()
                .map(Link::getHref)
                .map(l -> fagService.getFagResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<SkoleResource> getSkole(FaggruppeResource faggruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(faggruppe.getSkole()
                .stream()
                .map(Link::getHref)
                .map(l -> skoleService.getSkoleResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<SkolearResource> getSkolear(FaggruppeResource faggruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(faggruppe.getSkolear()
                .stream()
                .map(Link::getHref)
                .map(l -> skolearService.getSkolearResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<List<FaggruppemedlemskapResource>> getFaggruppemedlemskap(FaggruppeResource faggruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(faggruppe.getFaggruppemedlemskap()
                .stream()
                .map(Link::getHref)
                .map(l -> faggruppemedlemskapService.getFaggruppemedlemskapResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<MedlemskapResource>> getMedlemskap(FaggruppeResource faggruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(faggruppe.getMedlemskap()
                .stream()
                .map(Link::getHref)
                .map(l -> medlemskapService.getMedlemskapResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

}

