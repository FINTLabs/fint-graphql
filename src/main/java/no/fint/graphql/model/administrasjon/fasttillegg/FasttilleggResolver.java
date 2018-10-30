// Built from tag master

package no.fint.graphql.model.administrasjon.fasttillegg;

import com.coxautodev.graphql.tools.GraphQLResolver;
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


    public LonnsartResource getLonnsart(FasttilleggResource fasttillegg) {
        return lonnsartService.getLonnsartResource(Links.get(fasttillegg.getLonnsart()));
    }

    public PersonalressursResource getAnviser(FasttilleggResource fasttillegg) {
        return personalressursService.getPersonalressursResource(Links.get(fasttillegg.getAnviser()));
    }

    public PersonalressursResource getKonterer(FasttilleggResource fasttillegg) {
        return personalressursService.getPersonalressursResource(Links.get(fasttillegg.getKonterer()));
    }

    public PersonalressursResource getAttestant(FasttilleggResource fasttillegg) {
        return personalressursService.getPersonalressursResource(Links.get(fasttillegg.getAttestant()));
    }

    public ArbeidsforholdResource getArbeidsforhold(FasttilleggResource fasttillegg) {
        return arbeidsforholdService.getArbeidsforholdResource(Links.get(fasttillegg.getArbeidsforhold()));
    }

}

