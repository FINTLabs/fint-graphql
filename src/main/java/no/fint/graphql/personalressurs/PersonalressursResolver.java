package no.fint.graphql.personalressurs;

import com.coxautodev.graphql.tools.GraphQLResolver;
import no.fint.graphql.person.PersonService;
import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.felles.PersonResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PersonalressursResolver implements GraphQLResolver<PersonalressursResource> {

    @Autowired
    private PersonService personService;

    public PersonResource getPerson(PersonalressursResource personalressurs) {
        List<Link> personLinks = personalressurs.getLinks().get("person");
        if (personLinks == null || personLinks.isEmpty()) {
            return null;
        }

        return personService.getPersonResource(personLinks.get(0).getHref());
    }
}
