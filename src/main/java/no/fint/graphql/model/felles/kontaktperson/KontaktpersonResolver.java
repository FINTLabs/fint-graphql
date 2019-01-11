// Built from tag v3.1.0

package no.fint.graphql.model.felles.kontaktperson;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.felles.person.PersonService;


import no.fint.model.resource.Link;
import no.fint.model.resource.felles.KontaktpersonResource;
import no.fint.model.resource.felles.PersonResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component("fellesKontaktpersonResolver")
public class KontaktpersonResolver implements GraphQLResolver<KontaktpersonResource> {

    @Autowired
    private PersonService personService;


    public PersonResource getKontaktperson(KontaktpersonResource kontaktperson, DataFetchingEnvironment dfe) {
        return kontaktperson.getKontaktperson()
            .stream()
            .map(Link::getHref)
            .map(l -> personService.getPersonResource(l, dfe))
            .findFirst().orElse(null);
    }

    public PersonResource getPerson(KontaktpersonResource kontaktperson, DataFetchingEnvironment dfe) {
        return kontaktperson.getPerson()
            .stream()
            .map(Link::getHref)
            .map(l -> personService.getPersonResource(l, dfe))
            .findFirst().orElse(null);
    }

}

