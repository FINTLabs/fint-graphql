package no.fint.graphql.personalressurs;

import com.coxautodev.graphql.tools.GraphQLResolver;
import no.fint.graphql.Links;
import no.fint.graphql.arbeidsforhold.ArbeidsforholdService;
import no.fint.graphql.person.PersonService;
import no.fint.model.resource.administrasjon.personal.ArbeidsforholdResource;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.felles.PersonResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PersonalressursResolver implements GraphQLResolver<PersonalressursResource> {

    @Autowired
    private PersonService personService;

    @Autowired
    private ArbeidsforholdService arbeidsforholdService;

    public PersonResource getPerson(PersonalressursResource personalressurs) {
        return personService.getPersonResource(Links.get(personalressurs, "person"));
    }

    public ArbeidsforholdResource getArbeidsforhold(PersonalressursResource personalressurs) {
        return arbeidsforholdService.getArbeidsforholdResource(Links.get(personalressurs, "arbeidsforhold"));
    }
}
