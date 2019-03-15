
package no.fint.graphql.model.administrasjon.lonnsart;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.administrasjon.kodeverk.LonnsartResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("administrasjonLonnsartService")
public class LonnsartService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public LonnsartResource getLonnsartResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getLonnsartResource(
            endpoints.getAdministrasjonKodeverk() 
                + "/lonnsart/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public LonnsartResource getLonnsartResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, LonnsartResource.class, dfe);
    }
}

