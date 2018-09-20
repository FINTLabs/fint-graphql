package no.fint.graphql.person;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import no.fint.model.resource.felles.PersonResource;
import no.fint.model.resource.felles.PersonResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PersonQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private PersonService personService;

    public List<PersonResource> getPerson(String sinceTimeStamp) {
        PersonResources resources = personService.getPersonResources(sinceTimeStamp);
        return resources.getContent();
    }
}
