
package no.fint.graphql.model.administrasjon.variabellonn;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.administrasjon.lonnsart.LonnsartService;
import no.fint.graphql.model.administrasjon.arbeidsforhold.ArbeidsforholdService;
import no.fint.graphql.model.administrasjon.personalressurs.PersonalressursService;


import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.personal.VariabellonnResource;
import no.fint.model.resource.administrasjon.kodeverk.LonnsartResource;
import no.fint.model.resource.administrasjon.personal.ArbeidsforholdResource;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("administrasjonVariabellonnResolver")
public class VariabellonnResolver implements GraphQLResolver<VariabellonnResource> {

    @Autowired
    private LonnsartService lonnsartService;

    @Autowired
    private ArbeidsforholdService arbeidsforholdService;

    @Autowired
    private PersonalressursService personalressursService;


    public CompletionStage<LonnsartResource> getLonnsart(VariabellonnResource variabellonn, DataFetchingEnvironment dfe) {
        return Flux.fromStream(variabellonn.getLonnsart()
                .stream()
                .map(Link::getHref)
                .map(l -> lonnsartService.getLonnsartResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<ArbeidsforholdResource> getArbeidsforhold(VariabellonnResource variabellonn, DataFetchingEnvironment dfe) {
        return Flux.fromStream(variabellonn.getArbeidsforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> arbeidsforholdService.getArbeidsforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<PersonalressursResource> getAnviser(VariabellonnResource variabellonn, DataFetchingEnvironment dfe) {
        return Flux.fromStream(variabellonn.getAnviser()
                .stream()
                .map(Link::getHref)
                .map(l -> personalressursService.getPersonalressursResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<PersonalressursResource> getKonterer(VariabellonnResource variabellonn, DataFetchingEnvironment dfe) {
        return Flux.fromStream(variabellonn.getKonterer()
                .stream()
                .map(Link::getHref)
                .map(l -> personalressursService.getPersonalressursResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<PersonalressursResource> getAttestant(VariabellonnResource variabellonn, DataFetchingEnvironment dfe) {
        return Flux.fromStream(variabellonn.getAttestant()
                .stream()
                .map(Link::getHref)
                .map(l -> personalressursService.getPersonalressursResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

}

