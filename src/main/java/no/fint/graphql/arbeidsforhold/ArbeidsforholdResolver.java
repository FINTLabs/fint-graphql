package no.fint.graphql.arbeidsforhold;

import com.coxautodev.graphql.tools.GraphQLResolver;
import no.fint.graphql.Links;
import no.fint.graphql.organisasjonselement.OrganisasjonselementService;
import no.fint.graphql.personalressurs.PersonalressursService;
import no.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource;
import no.fint.model.resource.administrasjon.personal.ArbeidsforholdResource;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ArbeidsforholdResolver implements GraphQLResolver<ArbeidsforholdResource> {

    @Autowired
    private PersonalressursService personalressursService;

    @Autowired
    private OrganisasjonselementService organisasjonselementService;

    public PersonalressursResource getPersonalressurs(ArbeidsforholdResource arbeidsforhold) {
        return personalressursService.getPersonalressursResource(Links.get(arbeidsforhold, "personalressurs"));
    }

    public OrganisasjonselementResource getArbeidssted(ArbeidsforholdResource arbeidsforhold) {
        return organisasjonselementService.getOrganisasjonselementResource(Links.get(arbeidsforhold, "arbeidssted"));
    }
}
