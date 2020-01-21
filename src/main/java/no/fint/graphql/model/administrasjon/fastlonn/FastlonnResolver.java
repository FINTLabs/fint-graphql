
package no.fint.graphql.model.administrasjon.fastlonn;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.administrasjon.lonnsart.LonnsartService;
import no.fint.graphql.model.administrasjon.arbeidsforhold.ArbeidsforholdService;
import no.fint.graphql.model.administrasjon.personalressurs.PersonalressursService;


import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.personal.FastlonnResource;
import no.fint.model.resource.administrasjon.kodeverk.LonnsartResource;
import no.fint.model.resource.administrasjon.personal.ArbeidsforholdResource;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("administrasjonFastlonnResolver")
public class FastlonnResolver implements GraphQLResolver<FastlonnResource> {

    @Autowired
    private LonnsartService lonnsartService;

    @Autowired
    private ArbeidsforholdService arbeidsforholdService;

    @Autowired
    private PersonalressursService personalressursService;


    public CompletionStage<LonnsartResource> getLonnsart(FastlonnResource fastlonn, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fastlonn.getLonnsart()
                .stream()
                .map(Link::getHref)
                .map(l -> lonnsartService.getLonnsartResource(l, dfe)))
                .flatMap(Mono::flux)
                .singleOrEmpty()
                .toFuture();
    }

    public CompletionStage<ArbeidsforholdResource> getArbeidsforhold(FastlonnResource fastlonn, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fastlonn.getArbeidsforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> arbeidsforholdService.getArbeidsforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .singleOrEmpty()
                .toFuture();
    }

    public CompletionStage<PersonalressursResource> getAnviser(FastlonnResource fastlonn, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fastlonn.getAnviser()
                .stream()
                .map(Link::getHref)
                .map(l -> personalressursService.getPersonalressursResource(l, dfe)))
                .flatMap(Mono::flux)
                .singleOrEmpty()
                .toFuture();
    }

    public CompletionStage<PersonalressursResource> getKonterer(FastlonnResource fastlonn, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fastlonn.getKonterer()
                .stream()
                .map(Link::getHref)
                .map(l -> personalressursService.getPersonalressursResource(l, dfe)))
                .flatMap(Mono::flux)
                .singleOrEmpty()
                .toFuture();
    }

    public CompletionStage<PersonalressursResource> getAttestant(FastlonnResource fastlonn, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fastlonn.getAttestant()
                .stream()
                .map(Link::getHref)
                .map(l -> personalressursService.getPersonalressursResource(l, dfe)))
                .flatMap(Mono::flux)
                .singleOrEmpty()
                .toFuture();
    }

}

