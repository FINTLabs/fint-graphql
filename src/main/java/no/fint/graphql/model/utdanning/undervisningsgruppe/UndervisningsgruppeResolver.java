
package no.fint.graphql.model.utdanning.undervisningsgruppe;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.utdanning.elevforhold.ElevforholdService;
import no.fint.graphql.model.utdanning.fag.FagService;
import no.fint.graphql.model.utdanning.termin.TerminService;
import no.fint.graphql.model.utdanning.skole.SkoleService;
import no.fint.graphql.model.utdanning.skolear.SkolearService;
import no.fint.graphql.model.utdanning.undervisningsforhold.UndervisningsforholdService;
import no.fint.graphql.model.utdanning.time.TimeService;
import no.fint.graphql.model.utdanning.undervisningsgruppemedlemskap.UndervisningsgruppemedlemskapService;
import no.fint.graphql.model.utdanning.medlemskap.MedlemskapService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource;
import no.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.fint.model.resource.utdanning.timeplan.FagResource;
import no.fint.model.resource.utdanning.kodeverk.TerminResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.model.resource.utdanning.kodeverk.SkolearResource;
import no.fint.model.resource.utdanning.elev.UndervisningsforholdResource;
import no.fint.model.resource.utdanning.timeplan.TimeResource;
import no.fint.model.resource.utdanning.timeplan.UndervisningsgruppemedlemskapResource;
import no.fint.model.resource.utdanning.elev.MedlemskapResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("utdanningUndervisningsgruppeResolver")
public class UndervisningsgruppeResolver implements GraphQLResolver<UndervisningsgruppeResource> {

    @Autowired
    private ElevforholdService elevforholdService;

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

    @Autowired
    private MedlemskapService medlemskapService;


    public CompletionStage<List<ElevforholdResource>> getElevforhold(UndervisningsgruppeResource undervisningsgruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(undervisningsgruppe.getElevforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> elevforholdService.getElevforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

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

    public CompletionStage<List<MedlemskapResource>> getMedlemskap(UndervisningsgruppeResource undervisningsgruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(undervisningsgruppe.getMedlemskap()
                .stream()
                .map(Link::getHref)
                .map(l -> medlemskapService.getMedlemskapResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

}

