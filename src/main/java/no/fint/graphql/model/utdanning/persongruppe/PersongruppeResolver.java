
package no.fint.graphql.model.utdanning.persongruppe;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.utdanning.elev.ElevService;
import no.fint.graphql.model.utdanning.persongruppemedlemskap.PersongruppemedlemskapService;
import no.fint.graphql.model.utdanning.termin.TerminService;
import no.fint.graphql.model.utdanning.undervisningsforhold.UndervisningsforholdService;
import no.fint.graphql.model.utdanning.skole.SkoleService;
import no.fint.graphql.model.utdanning.skoleressurs.SkoleressursService;
import no.fint.graphql.model.utdanning.skolear.SkolearService;
import no.fint.graphql.model.utdanning.medlemskap.MedlemskapService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.elev.PersongruppeResource;
import no.fint.model.resource.utdanning.elev.ElevResource;
import no.fint.model.resource.utdanning.elev.PersongruppemedlemskapResource;
import no.fint.model.resource.utdanning.kodeverk.TerminResource;
import no.fint.model.resource.utdanning.elev.UndervisningsforholdResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.model.resource.utdanning.elev.SkoleressursResource;
import no.fint.model.resource.utdanning.kodeverk.SkolearResource;
import no.fint.model.resource.utdanning.elev.MedlemskapResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("utdanningPersongruppeResolver")
public class PersongruppeResolver implements GraphQLResolver<PersongruppeResource> {

    @Autowired
    private ElevService elevService;

    @Autowired
    private PersongruppemedlemskapService persongruppemedlemskapService;

    @Autowired
    private TerminService terminService;

    @Autowired
    private UndervisningsforholdService undervisningsforholdService;

    @Autowired
    private SkoleService skoleService;

    @Autowired
    private SkoleressursService skoleressursService;

    @Autowired
    private SkolearService skolearService;

    @Autowired
    private MedlemskapService medlemskapService;


    public CompletionStage<List<ElevResource>> getElev(PersongruppeResource persongruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(persongruppe.getElev()
                .stream()
                .map(Link::getHref)
                .map(l -> elevService.getElevResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<PersongruppemedlemskapResource>> getPersongruppemedlemskap(PersongruppeResource persongruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(persongruppe.getPersongruppemedlemskap()
                .stream()
                .map(Link::getHref)
                .map(l -> persongruppemedlemskapService.getPersongruppemedlemskapResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<TerminResource>> getTermin(PersongruppeResource persongruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(persongruppe.getTermin()
                .stream()
                .map(Link::getHref)
                .map(l -> terminService.getTerminResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<UndervisningsforholdResource>> getUndervisningsforhold(PersongruppeResource persongruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(persongruppe.getUndervisningsforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> undervisningsforholdService.getUndervisningsforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<SkoleResource>> getSkole(PersongruppeResource persongruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(persongruppe.getSkole()
                .stream()
                .map(Link::getHref)
                .map(l -> skoleService.getSkoleResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<SkoleressursResource>> getSkoleressurs(PersongruppeResource persongruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(persongruppe.getSkoleressurs()
                .stream()
                .map(Link::getHref)
                .map(l -> skoleressursService.getSkoleressursResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<SkolearResource>> getSkolear(PersongruppeResource persongruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(persongruppe.getSkolear()
                .stream()
                .map(Link::getHref)
                .map(l -> skolearService.getSkolearResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<MedlemskapResource>> getMedlemskap(PersongruppeResource persongruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(persongruppe.getMedlemskap()
                .stream()
                .map(Link::getHref)
                .map(l -> medlemskapService.getMedlemskapResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

}

