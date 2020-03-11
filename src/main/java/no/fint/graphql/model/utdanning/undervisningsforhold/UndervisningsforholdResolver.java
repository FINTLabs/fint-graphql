
package no.fint.graphql.model.utdanning.undervisningsforhold;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.administrasjon.arbeidsforhold.ArbeidsforholdService;
import no.fint.graphql.model.utdanning.basisgruppe.BasisgruppeService;
import no.fint.graphql.model.utdanning.kontaktlarergruppe.KontaktlarergruppeService;
import no.fint.graphql.model.utdanning.undervisningsgruppe.UndervisningsgruppeService;
import no.fint.graphql.model.utdanning.eksamensgruppe.EksamensgruppeService;
import no.fint.graphql.model.utdanning.time.TimeService;
import no.fint.graphql.model.utdanning.skole.SkoleService;
import no.fint.graphql.model.utdanning.skoleressurs.SkoleressursService;
import no.fint.graphql.model.utdanning.medlemskap.MedlemskapService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.elev.UndervisningsforholdResource;
import no.fint.model.resource.administrasjon.personal.ArbeidsforholdResource;
import no.fint.model.resource.utdanning.elev.BasisgruppeResource;
import no.fint.model.resource.utdanning.elev.KontaktlarergruppeResource;
import no.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource;
import no.fint.model.resource.utdanning.vurdering.EksamensgruppeResource;
import no.fint.model.resource.utdanning.timeplan.TimeResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.model.resource.utdanning.elev.SkoleressursResource;
import no.fint.model.resource.utdanning.elev.MedlemskapResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("utdanningUndervisningsforholdResolver")
public class UndervisningsforholdResolver implements GraphQLResolver<UndervisningsforholdResource> {

    @Autowired
    private ArbeidsforholdService arbeidsforholdService;

    @Autowired
    private BasisgruppeService basisgruppeService;

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

    @Autowired
    private MedlemskapService medlemskapService;


    public CompletionStage<ArbeidsforholdResource> getArbeidsforhold(UndervisningsforholdResource undervisningsforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(undervisningsforhold.getArbeidsforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> arbeidsforholdService.getArbeidsforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<List<BasisgruppeResource>> getBasisgruppe(UndervisningsforholdResource undervisningsforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(undervisningsforhold.getBasisgruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> basisgruppeService.getBasisgruppeResource(l, dfe)))
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

    public CompletionStage<List<MedlemskapResource>> getMedlemskap(UndervisningsforholdResource undervisningsforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(undervisningsforhold.getMedlemskap()
                .stream()
                .map(Link::getHref)
                .map(l -> medlemskapService.getMedlemskapResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

}

