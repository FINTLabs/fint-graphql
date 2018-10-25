// Built from tag v3.1.0

package no.fint.graphql.model.felles.person;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import no.fint.model.resource.felles.PersonResource;
import no.fint.model.resource.felles.PersonResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("fellesPersonQueryResolver")
public class PersonQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private PersonService service;

    public List<PersonResource> getPerson(String sinceTimeStamp) {
        PersonResources resources = service.getPersonResources(sinceTimeStamp);
        return resources.getContent();
    }
}
