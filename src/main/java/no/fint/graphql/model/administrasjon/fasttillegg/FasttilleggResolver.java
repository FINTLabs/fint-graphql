// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.fasttillegg;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.Links;




import no.fint.graphql.model.administrasjon.lonnsart.LonnsartService;
import no.fint.graphql.model.administrasjon.personalressurs.PersonalressursService;
import no.fint.graphql.model.administrasjon.arbeidsforhold.ArbeidsforholdService;


import no.fint.model.resource.administrasjon.personal.FasttilleggResource;


import no.fint.model.resource.administrasjon.kodeverk.LonnsartResource;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.administrasjon.personal.ArbeidsforholdResource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("administrasjonFasttilleggResolver")
public class FasttilleggResolver implements GraphQLResolver<FasttilleggResource> {


    @Autowired
    private LonnsartService lonnsartService;

    @Autowired
    private PersonalressursService personalressursService;

    @Autowired
    private ArbeidsforholdService arbeidsforholdService;


    public LonnsartResource getLonnsart(FasttilleggResource fasttillegg, DataFetchingEnvironment dfe) {
        return lonnsartService.getLonnsartResource(
            Links.get(fasttillegg.getLonnsart()),
            dfe);
    }

    public PersonalressursResource getAnviser(FasttilleggResource fasttillegg, DataFetchingEnvironment dfe) {
        return personalressursService.getPersonalressursResource(
            Links.get(fasttillegg.getAnviser()),
            dfe);
    }

    public PersonalressursResource getKonterer(FasttilleggResource fasttillegg, DataFetchingEnvironment dfe) {
        return personalressursService.getPersonalressursResource(
            Links.get(fasttillegg.getKonterer()),
            dfe);
    }

    public PersonalressursResource getAttestant(FasttilleggResource fasttillegg, DataFetchingEnvironment dfe) {
        return personalressursService.getPersonalressursResource(
            Links.get(fasttillegg.getAttestant()),
            dfe);
    }

    public ArbeidsforholdResource getArbeidsforhold(FasttilleggResource fasttillegg, DataFetchingEnvironment dfe) {
        return arbeidsforholdService.getArbeidsforholdResource(
            Links.get(fasttillegg.getArbeidsforhold()),
            dfe);
    }

}

