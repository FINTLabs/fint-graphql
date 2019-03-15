// Built from tag release-3.2

package no.fint.graphql.model.felles.person;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.felles.PersonResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("fellesPersonService")
public class PersonService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public PersonResource getPersonResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getPersonResource(
            endpoints.getFelles() 
                + "/person/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public PersonResource getPersonResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, PersonResource.class, dfe);
    }
}

