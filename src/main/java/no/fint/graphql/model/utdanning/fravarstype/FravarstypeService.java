// Built from tag release-3.2

package no.fint.graphql.model.utdanning.fravarstype;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.kodeverk.FravarstypeResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("utdanningFravarstypeService")
public class FravarstypeService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public FravarstypeResource getFravarstypeResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getFravarstypeResource(
            endpoints.getUtdanningKodeverk() 
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

