// Built from tag release-3.2

package no.fint.graphql.model.felles.person;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.felles.PersonResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Stream;

@Service("fellesPersonService")
public class PersonService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public PersonResource getPersonResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return Stream
                .of(endpoints.getAdministrasjonPersonal(), endpoints.getUtdanningElev(), endpoints.getFelles())
                .map(e -> e + "/person/"
                        + id
                        + "/"
                        + value)
                .map(r -> getPersonResource(r, dfe))
                .filter(Objects::nonNull)
                .findAny()
                .orElse(null);
    }

    public PersonResource getPersonResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, PersonResource.class, dfe);
    }
}

