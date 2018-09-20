package no.fint.graphql.person;

import com.coxautodev.graphql.tools.GraphQLResolver;
import no.fint.graphql.Links;
import no.fint.graphql.personalressurs.PersonalressursService;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.felles.PersonResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonResolver implements GraphQLResolver<PersonResource> {

    @Autowired
    private PersonalressursService personalressursService;

    public PersonalressursResource getPersonalressurs(PersonResource person) {
        return personalressursService.getPersonalressursResource(Links.get(person, "personalressurs "));
    }

}
