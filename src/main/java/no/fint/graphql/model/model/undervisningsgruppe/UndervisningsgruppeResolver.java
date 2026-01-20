
package no.fint.graphql.model.model.undervisningsgruppe;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.model.fag.FagService;
import no.fint.graphql.model.model.termin.TerminService;
import no.fint.graphql.model.model.skole.SkoleService;
import no.fint.graphql.model.model.skolear.SkolearService;
import no.fint.graphql.model.model.undervisningsforhold.UndervisningsforholdService;
import no.fint.graphql.model.model.time.TimeService;
import no.fint.graphql.model.model.undervisningsgruppemedlemskap.UndervisningsgruppemedlemskapService;


import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource;
import no.novari.fint.model.resource.utdanning.timeplan.FagResource;
import no.novari.fint.model.resource.utdanning.kodeverk.TerminResource;
import no.novari.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.novari.fint.model.resource.utdanning.kodeverk.SkolearResource;
import no.novari.fint.model.resource.utdanning.elev.UndervisningsforholdResource;
import no.novari.fint.model.resource.utdanning.timeplan.TimeResource;
import no.novari.fint.model.resource.utdanning.timeplan.UndervisningsgruppemedlemskapResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("modelUndervisningsgruppeResolver")
public class UndervisningsgruppeResolver implements GraphQLResolver<UndervisningsgruppeResource> {

    @Autowired
    private FagService fagService;

    @Autowired
    private TerminService terminService;

    @Autowired
    private SkoleService skoleService;

    @Autowired
    private SkolearService skolearService;

    @Autowired
    private UndervisningsforholdService undervisningsforholdService;

    @Autowired
    private TimeService timeService;

    @Autowired
    private UndervisningsgruppemedlemskapService undervisningsgruppemedlemskapService;


    public CompletionStage<List<FagResource>> getFag(UndervisningsgruppeResource undervisningsgruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(undervisningsgruppe.getFag()
                .stream()
                .map(Link::getHref)
                .map(l -> fagService.getFagResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<TerminResource>> getTermin(UndervisningsgruppeResource undervisningsgruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(undervisningsgruppe.getTermin()
                .stream()
                .map(Link::getHref)
                .map(l -> terminService.getTerminResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<SkoleResource> getSkole(UndervisningsgruppeResource undervisningsgruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(undervisningsgruppe.getSkole()
                .stream()
                .map(Link::getHref)
                .map(l -> skoleService.getSkoleResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<SkolearResource> getSkolear(UndervisningsgruppeResource undervisningsgruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(undervisningsgruppe.getSkolear()
                .stream()
                .map(Link::getHref)
                .map(l -> skolearService.getSkolearResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<List<UndervisningsforholdResource>> getUndervisningsforhold(UndervisningsgruppeResource undervisningsgruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(undervisningsgruppe.getUndervisningsforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> undervisningsforholdService.getUndervisningsforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<TimeResource>> getTime(UndervisningsgruppeResource undervisningsgruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(undervisningsgruppe.getTime()
                .stream()
                .map(Link::getHref)
                .map(l -> timeService.getTimeResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<UndervisningsgruppemedlemskapResource>> getGruppemedlemskap(UndervisningsgruppeResource undervisningsgruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(undervisningsgruppe.getGruppemedlemskap()
                .stream()
                .map(Link::getHref)
                .map(l -> undervisningsgruppemedlemskapService.getUndervisningsgruppemedlemskapResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

}

