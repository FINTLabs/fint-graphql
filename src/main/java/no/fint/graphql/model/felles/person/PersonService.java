// Built from tag v3.1.0

package no.fint.graphql.model.felles.person;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.felles.PersonResource;
import no.fint.model.resource.felles.PersonResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("fellesPersonService")
public class PersonService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public PersonResources getPersonResources(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        return webClientRequest.get(
                ResourceUrlBuilder.urlWithQueryParams(
                    endpoints.getFelles() + "/person",
                    sinceTimeStamp),
                PersonResources.class,
                dfe);
    }

    public PersonResource getPersonResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, PersonResource.class, dfe);
    }
}

