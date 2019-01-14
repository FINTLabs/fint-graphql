// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.fastlonn;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.administrasjon.lonnsart.LonnsartService;
import no.fint.graphql.model.administrasjon.personalressurs.PersonalressursService;
import no.fint.graphql.model.administrasjon.arbeidsforhold.ArbeidsforholdService;


import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.personal.FastlonnResource;
import no.fint.model.resource.administrasjon.kodeverk.LonnsartResource;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.administrasjon.personal.ArbeidsforholdResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component("administrasjonFastlonnResolver")
public class FastlonnResolver implements GraphQLResolver<FastlonnResource> {

    @Autowired
    private LonnsartService lonnsartService;

    @Autowired
    private PersonalressursService personalressursService;

    @Autowired
    private ArbeidsforholdService arbeidsforholdService;


    public LonnsartResource getLonnsart(FastlonnResource fastlonn, DataFetchingEnvironment dfe) {
        return fastlonn.getLonnsart()
                .stream()
                .map(Link::getHref)
                .map(l -> lonnsartService.getLonnsartResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public PersonalressursResource getAnviser(FastlonnResource fastlonn, DataFetchingEnvironment dfe) {
        return fastlonn.getAnviser()
                .stream()
                .map(Link::getHref)
                .map(l -> personalressursService.getPersonalressursResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public PersonalressursResource getKonterer(FastlonnResource fastlonn, DataFetchingEnvironment dfe) {
        return fastlonn.getKonterer()
                .stream()
                .map(Link::getHref)
                .map(l -> personalressursService.getPersonalressursResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public PersonalressursResource getAttestant(FastlonnResource fastlonn, DataFetchingEnvironment dfe) {
        return fastlonn.getAttestant()
                .stream()
                .map(Link::getHref)
                .map(l -> personalressursService.getPersonalressursResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public ArbeidsforholdResource getArbeidsforhold(FastlonnResource fastlonn, DataFetchingEnvironment dfe) {
        return fastlonn.getArbeidsforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> arbeidsforholdService.getArbeidsforholdResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

}

