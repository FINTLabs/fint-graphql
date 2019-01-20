// Built from tag v3.1.0

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
        PersonResource personResource = getPersonResource(
                endpoints.getAdministrasjonPersonal()
                        + "/person/"
                        + id
                        + "/"
                        + value,
                dfe);
        if (personResource == null) {
            personResource = getPersonResource(
                    endpoints.getUtdanningElev()
                            + "/person/"
                            + id
                            + "/"
                            + value,
                    dfe);
        }

        return personResource;

    }

    public PersonResource getPersonResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, PersonResource.class, dfe);
    }
}

