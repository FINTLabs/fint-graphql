
package no.fint.graphql.model.model.klasse;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.model.skolear.SkolearService;
import no.fint.graphql.model.model.termin.TerminService;
import no.fint.graphql.model.model.arstrinn.ArstrinnService;
import no.fint.graphql.model.model.skole.SkoleService;
import no.fint.graphql.model.model.undervisningsforhold.UndervisningsforholdService;
import no.fint.graphql.model.model.klassemedlemskap.KlassemedlemskapService;
import no.fint.graphql.model.model.kontaktlarergruppe.KontaktlarergruppeService;


import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.utdanning.elev.KlasseResource;
import no.novari.fint.model.resource.utdanning.kodeverk.SkolearResource;
import no.novari.fint.model.resource.utdanning.kodeverk.TerminResource;
import no.novari.fint.model.resource.utdanning.utdanningsprogram.ArstrinnResource;
import no.novari.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.novari.fint.model.resource.utdanning.elev.UndervisningsforholdResource;
import no.novari.fint.model.resource.utdanning.elev.KlassemedlemskapResource;
import no.novari.fint.model.resource.utdanning.elev.KontaktlarergruppeResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("modelKlasseResolver")
public class KlasseResolver implements GraphQLResolver<KlasseResource> {

    @Autowired
    private SkolearService skolearService;

    @Autowired
    private TerminService terminService;

    @Autowired
    private ArstrinnService arstrinnService;

    @Autowired
    private SkoleService skoleService;

    @Autowired
    private UndervisningsforholdService undervisningsforholdService;

    @Autowired
    private KlassemedlemskapService klassemedlemskapService;

    @Autowired
    private KontaktlarergruppeService kontaktlarergruppeService;


    public CompletionStage<SkolearResource> getSkolear(KlasseResource klasse, DataFetchingEnvironment dfe) {
        return Flux.fromStream(klasse.getSkolear()
                .stream()
                .map(Link::getHref)
                .map(l -> skolearService.getSkolearResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<List<TerminResource>> getTermin(KlasseResource klasse, DataFetchingEnvironment dfe) {
        return Flux.fromStream(klasse.getTermin()
                .stream()
                .map(Link::getHref)
                .map(l -> terminService.getTerminResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<ArstrinnResource> getTrinn(KlasseResource klasse, DataFetchingEnvironment dfe) {
        return Flux.fromStream(klasse.getTrinn()
                .stream()
                .map(Link::getHref)
                .map(l -> arstrinnService.getArstrinnResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<SkoleResource> getSkole(KlasseResource klasse, DataFetchingEnvironment dfe) {
        return Flux.fromStream(klasse.getSkole()
                .stream()
                .map(Link::getHref)
                .map(l -> skoleService.getSkoleResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<List<UndervisningsforholdResource>> getUndervisningsforhold(KlasseResource klasse, DataFetchingEnvironment dfe) {
        return Flux.fromStream(klasse.getUndervisningsforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> undervisningsforholdService.getUndervisningsforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<KlassemedlemskapResource>> getKlassemedlemskap(KlasseResource klasse, DataFetchingEnvironment dfe) {
        return Flux.fromStream(klasse.getKlassemedlemskap()
                .stream()
                .map(Link::getHref)
                .map(l -> klassemedlemskapService.getKlassemedlemskapResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<KontaktlarergruppeResource>> getKontaktlarergruppe(KlasseResource klasse, DataFetchingEnvironment dfe) {
        return Flux.fromStream(klasse.getKontaktlarergruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> kontaktlarergruppeService.getKontaktlarergruppeResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

}

