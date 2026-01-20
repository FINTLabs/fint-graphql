
package no.fint.graphql.model.model.undervisningsforhold;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.model.arbeidsforhold.ArbeidsforholdService;
import no.fint.graphql.model.model.klasse.KlasseService;
import no.fint.graphql.model.model.kontaktlarergruppe.KontaktlarergruppeService;
import no.fint.graphql.model.model.undervisningsgruppe.UndervisningsgruppeService;
import no.fint.graphql.model.model.eksamensgruppe.EksamensgruppeService;
import no.fint.graphql.model.model.time.TimeService;
import no.fint.graphql.model.model.skole.SkoleService;
import no.fint.graphql.model.model.skoleressurs.SkoleressursService;


import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.utdanning.elev.UndervisningsforholdResource;
import no.novari.fint.model.resource.administrasjon.personal.ArbeidsforholdResource;
import no.novari.fint.model.resource.utdanning.elev.KlasseResource;
import no.novari.fint.model.resource.utdanning.elev.KontaktlarergruppeResource;
import no.novari.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource;
import no.novari.fint.model.resource.utdanning.vurdering.EksamensgruppeResource;
import no.novari.fint.model.resource.utdanning.timeplan.TimeResource;
import no.novari.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.novari.fint.model.resource.utdanning.elev.SkoleressursResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("modelUndervisningsforholdResolver")
public class UndervisningsforholdResolver implements GraphQLResolver<UndervisningsforholdResource> {

    @Autowired
    private ArbeidsforholdService arbeidsforholdService;

    @Autowired
    private KlasseService klasseService;

    @Autowired
    private KontaktlarergruppeService kontaktlarergruppeService;

    @Autowired
    private UndervisningsgruppeService undervisningsgruppeService;

    @Autowired
    private EksamensgruppeService eksamensgruppeService;

    @Autowired
    private TimeService timeService;

    @Autowired
    private SkoleService skoleService;

    @Autowired
    private SkoleressursService skoleressursService;


    public CompletionStage<ArbeidsforholdResource> getArbeidsforhold(UndervisningsforholdResource undervisningsforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(undervisningsforhold.getArbeidsforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> arbeidsforholdService.getArbeidsforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<List<KlasseResource>> getKlasse(UndervisningsforholdResource undervisningsforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(undervisningsforhold.getKlasse()
                .stream()
                .map(Link::getHref)
                .map(l -> klasseService.getKlasseResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<KontaktlarergruppeResource>> getKontaktlarergruppe(UndervisningsforholdResource undervisningsforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(undervisningsforhold.getKontaktlarergruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> kontaktlarergruppeService.getKontaktlarergruppeResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<UndervisningsgruppeResource>> getUndervisningsgruppe(UndervisningsforholdResource undervisningsforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(undervisningsforhold.getUndervisningsgruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> undervisningsgruppeService.getUndervisningsgruppeResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<EksamensgruppeResource>> getEksamensgruppe(UndervisningsforholdResource undervisningsforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(undervisningsforhold.getEksamensgruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> eksamensgruppeService.getEksamensgruppeResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<TimeResource>> getTime(UndervisningsforholdResource undervisningsforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(undervisningsforhold.getTime()
                .stream()
                .map(Link::getHref)
                .map(l -> timeService.getTimeResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<SkoleResource> getSkole(UndervisningsforholdResource undervisningsforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(undervisningsforhold.getSkole()
                .stream()
                .map(Link::getHref)
                .map(l -> skoleService.getSkoleResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<SkoleressursResource> getSkoleressurs(UndervisningsforholdResource undervisningsforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(undervisningsforhold.getSkoleressurs()
                .stream()
                .map(Link::getHref)
                .map(l -> skoleressursService.getSkoleressursResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

}

