
package no.fint.graphql.model.model.person;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.felles.PersonResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelPersonService")
public class PersonService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<PersonResource> getPersonResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getPersonResource(
            endpoints.getFelles() 
                + "/person/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<PersonResource> getPersonResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, PersonResource.class, dfe);
    }
}

