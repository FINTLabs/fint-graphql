// Built from tag release-3.2

package no.fint.graphql.model.administrasjon.fravarstype;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.administrasjon.kodeverk.FravarstypeResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("administrasjonFravarstypeService")
public class FravarstypeService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public FravarstypeResource getFravarstypeResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getFravarstypeResource(
            endpoints.getAdministrasjonKodeverk() 
                + "/fravarstype/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public FravarstypeResource getFravarstypeResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, FravarstypeResource.class, dfe);
    }
}

