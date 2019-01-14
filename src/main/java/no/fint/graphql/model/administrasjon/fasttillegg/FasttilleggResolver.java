// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.fasttillegg;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.administrasjon.lonnsart.LonnsartService;
import no.fint.graphql.model.administrasjon.personalressurs.PersonalressursService;
import no.fint.graphql.model.administrasjon.arbeidsforhold.ArbeidsforholdService;


import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.personal.FasttilleggResource;
import no.fint.model.resource.administrasjon.kodeverk.LonnsartResource;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.administrasjon.personal.ArbeidsforholdResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component("administrasjonFasttilleggResolver")
public class FasttilleggResolver implements GraphQLResolver<FasttilleggResource> {

    @Autowired
    private LonnsartService lonnsartService;

    @Autowired
    private PersonalressursService personalressursService;

    @Autowired
    private ArbeidsforholdService arbeidsforholdService;


    public LonnsartResource getLonnsart(FasttilleggResource fasttillegg, DataFetchingEnvironment dfe) {
        return fasttillegg.getLonnsart()
                .stream()
                .map(Link::getHref)
                .map(l -> lonnsartService.getLonnsartResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public PersonalressursResource getAnviser(FasttilleggResource fasttillegg, DataFetchingEnvironment dfe) {
        return fasttillegg.getAnviser()
                .stream()
                .map(Link::getHref)
                .map(l -> personalressursService.getPersonalressursResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public PersonalressursResource getKonterer(FasttilleggResource fasttillegg, DataFetchingEnvironment dfe) {
        return fasttillegg.getKonterer()
                .stream()
                .map(Link::getHref)
                .map(l -> personalressursService.getPersonalressursResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public PersonalressursResource getAttestant(FasttilleggResource fasttillegg, DataFetchingEnvironment dfe) {
        return fasttillegg.getAttestant()
                .stream()
                .map(Link::getHref)
                .map(l -> personalressursService.getPersonalressursResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public ArbeidsforholdResource getArbeidsforhold(FasttilleggResource fasttillegg, DataFetchingEnvironment dfe) {
        return fasttillegg.getArbeidsforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> arbeidsforholdService.getArbeidsforholdResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

}

