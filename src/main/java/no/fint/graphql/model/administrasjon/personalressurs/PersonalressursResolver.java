
package no.fint.graphql.model.administrasjon.personalressurs;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.administrasjon.personalressurskategori.PersonalressurskategoriService;
import no.fint.graphql.model.administrasjon.arbeidsforhold.ArbeidsforholdService;
import no.fint.graphql.model.felles.person.PersonService;
import no.fint.graphql.model.administrasjon.fullmakt.FullmaktService;
import no.fint.graphql.model.administrasjon.organisasjonselement.OrganisasjonselementService;
import no.fint.graphql.model.utdanning.skoleressurs.SkoleressursService;


import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.administrasjon.kodeverk.PersonalressurskategoriResource;
import no.fint.model.resource.administrasjon.personal.ArbeidsforholdResource;
import no.fint.model.resource.felles.PersonResource;
import no.fint.model.resource.administrasjon.fullmakt.FullmaktResource;
import no.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource;
import no.fint.model.resource.utdanning.elev.SkoleressursResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("administrasjonPersonalressursResolver")
public class PersonalressursResolver implements GraphQLResolver<PersonalressursResource> {

    @Autowired
    private PersonalressurskategoriService personalressurskategoriService;

    @Autowired
    private ArbeidsforholdService arbeidsforholdService;

    @Autowired
    private PersonService personService;

    @Autowired
    private FullmaktService fullmaktService;

    @Autowired
    private OrganisasjonselementService organisasjonselementService;

    @Autowired
    private SkoleressursService skoleressursService;


    public CompletionStage<PersonalressurskategoriResource> getPersonalressurskategori(PersonalressursResource personalressurs, DataFetchingEnvironment dfe) {
        return Flux.fromStream(personalressurs.getPersonalressurskategori()
                .stream()
                .map(Link::getHref)
                .map(l -> personalressurskategoriService.getPersonalressurskategoriResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<List<ArbeidsforholdResource>> getArbeidsforhold(PersonalressursResource personalressurs, DataFetchingEnvironment dfe) {
        return Flux.fromStream(personalressurs.getArbeidsforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> arbeidsforholdService.getArbeidsforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<PersonResource> getPerson(PersonalressursResource personalressurs, DataFetchingEnvironment dfe) {
        return Flux.fromStream(personalressurs.getPerson()
                .stream()
                .map(Link::getHref)
                .map(l -> personService.getPersonResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<List<FullmaktResource>> getStedfortreder(PersonalressursResource personalressurs, DataFetchingEnvironment dfe) {
        return Flux.fromStream(personalressurs.getStedfortreder()
                .stream()
                .map(Link::getHref)
                .map(l -> fullmaktService.getFullmaktResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<FullmaktResource>> getFullmakt(PersonalressursResource personalressurs, DataFetchingEnvironment dfe) {
        return Flux.fromStream(personalressurs.getFullmakt()
                .stream()
                .map(Link::getHref)
                .map(l -> fullmaktService.getFullmaktResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<OrganisasjonselementResource>> getLeder(PersonalressursResource personalressurs, DataFetchingEnvironment dfe) {
        return Flux.fromStream(personalressurs.getLeder()
                .stream()
                .map(Link::getHref)
                .map(l -> organisasjonselementService.getOrganisasjonselementResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<ArbeidsforholdResource>> getPersonalansvar(PersonalressursResource personalressurs, DataFetchingEnvironment dfe) {
        return Flux.fromStream(personalressurs.getPersonalansvar()
                .stream()
                .map(Link::getHref)
                .map(l -> arbeidsforholdService.getArbeidsforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<SkoleressursResource> getSkoleressurs(PersonalressursResource personalressurs, DataFetchingEnvironment dfe) {
        return Flux.fromStream(personalressurs.getSkoleressurs()
                .stream()
                .map(Link::getHref)
                .map(l -> skoleressursService.getSkoleressursResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

}

