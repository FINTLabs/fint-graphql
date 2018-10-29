// Built from tag v3.1.0

package no.fint.graphql.model.felles.kontaktperson;

import com.coxautodev.graphql.tools.GraphQLResolver;
import no.fint.graphql.Links;




import no.fint.graphql.model.felles.person.PersonService;


import no.fint.model.resource.felles.KontaktpersonResource;


import no.fint.model.resource.felles.PersonResource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("fellesKontaktpersonResolver")
public class KontaktpersonResolver implements GraphQLResolver<KontaktpersonResource> {


    @Autowired
    private PersonService personService;


    public PersonResource getKontaktperson(KontaktpersonResource kontaktperson) {
        return personService.getPersonResource(Links.get(kontaktperson, "kontaktperson"));
    }

    public PersonResource getPerson(KontaktpersonResource kontaktperson) {
        return personService.getPersonResource(Links.get(kontaktperson, "person"));
    }

}

