// Built from tag release-3.2

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

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component("administrasjonVariabellonnResolver")
public class VariabellonnResolver implements GraphQLResolver<VariabellonnResource> {

    @Autowired
    private LonnsartService lonnsartService;

    @Autowired
    private ArbeidsforholdService arbeidsforholdService;

    @Autowired
    private PersonalressursService personalressursService;


    public LonnsartResource getLonnsart(VariabellonnResource variabellonn, DataFetchingEnvironment dfe) {
        return variabellonn.getLonnsart()
                .stream()
                .map(Link::getHref)
                .map(l -> lonnsartService.getLonnsartResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public ArbeidsforholdResource getArbeidsforhold(VariabellonnResource variabellonn, DataFetchingEnvironment dfe) {
        return variabellonn.getArbeidsforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> arbeidsforholdService.getArbeidsforholdResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public PersonalressursResource getAnviser(VariabellonnResource variabellonn, DataFetchingEnvironment dfe) {
        return variabellonn.getAnviser()
                .stream()
                .map(Link::getHref)
                .map(l -> personalressursService.getPersonalressursResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public PersonalressursResource getKonterer(VariabellonnResource variabellonn, DataFetchingEnvironment dfe) {
        return variabellonn.getKonterer()
                .stream()
                .map(Link::getHref)
                .map(l -> personalressursService.getPersonalressursResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public PersonalressursResource getAttestant(VariabellonnResource variabellonn, DataFetchingEnvironment dfe) {
        return variabellonn.getAttestant()
                .stream()
                .map(Link::getHref)
                .map(l -> personalressursService.getPersonalressursResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

}

