// Built from tag v3.1.0

package no.fint.graphql.model.felles.kontaktperson;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.felles.KontaktpersonResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("fellesKontaktpersonService")
public class KontaktpersonService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public KontaktpersonResource getKontaktpersonResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getKontaktpersonResource(
            endpoints.getFelles() 
                + "/kontaktperson/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public KontaktpersonResource getKontaktpersonResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, KontaktpersonResource.class, dfe);
    }
}

