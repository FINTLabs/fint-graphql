
package no.fint.graphql.model.model.kontaktlarergruppe;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.model.klasse.KlasseService;
import no.fint.graphql.model.model.termin.TerminService;
import no.fint.graphql.model.model.skole.SkoleService;
import no.fint.graphql.model.model.skolear.SkolearService;
import no.fint.graphql.model.model.undervisningsforhold.UndervisningsforholdService;
import no.fint.graphql.model.model.kontaktlarergruppemedlemskap.KontaktlarergruppemedlemskapService;


import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.utdanning.elev.KontaktlarergruppeResource;
import no.novari.fint.model.resource.utdanning.elev.KlasseResource;
import no.novari.fint.model.resource.utdanning.kodeverk.TerminResource;
import no.novari.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.novari.fint.model.resource.utdanning.kodeverk.SkolearResource;
import no.novari.fint.model.resource.utdanning.elev.UndervisningsforholdResource;
import no.novari.fint.model.resource.utdanning.elev.KontaktlarergruppemedlemskapResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("modelKontaktlarergruppeResolver")
public class KontaktlarergruppeResolver implements GraphQLResolver<KontaktlarergruppeResource> {

    @Autowired
    private KlasseService klasseService;

    @Autowired
    private TerminService terminService;

    @Autowired
    private SkoleService skoleService;

    @Autowired
    private SkolearService skolearService;

    @Autowired
    private UndervisningsforholdService undervisningsforholdService;

    @Autowired
    private KontaktlarergruppemedlemskapService kontaktlarergruppemedlemskapService;


    public CompletionStage<List<KlasseResource>> getKlasse(KontaktlarergruppeResource kontaktlarergruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(kontaktlarergruppe.getKlasse()
                .stream()
                .map(Link::getHref)
                .map(l -> klasseService.getKlasseResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<TerminResource>> getTermin(KontaktlarergruppeResource kontaktlarergruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(kontaktlarergruppe.getTermin()
                .stream()
                .map(Link::getHref)
                .map(l -> terminService.getTerminResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<SkoleResource> getSkole(KontaktlarergruppeResource kontaktlarergruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(kontaktlarergruppe.getSkole()
                .stream()
                .map(Link::getHref)
                .map(l -> skoleService.getSkoleResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<SkolearResource> getSkolear(KontaktlarergruppeResource kontaktlarergruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(kontaktlarergruppe.getSkolear()
                .stream()
                .map(Link::getHref)
                .map(l -> skolearService.getSkolearResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<List<UndervisningsforholdResource>> getUndervisningsforhold(KontaktlarergruppeResource kontaktlarergruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(kontaktlarergruppe.getUndervisningsforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> undervisningsforholdService.getUndervisningsforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<KontaktlarergruppemedlemskapResource>> getGruppemedlemskap(KontaktlarergruppeResource kontaktlarergruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(kontaktlarergruppe.getGruppemedlemskap()
                .stream()
                .map(Link::getHref)
                .map(l -> kontaktlarergruppemedlemskapService.getKontaktlarergruppemedlemskapResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

}

