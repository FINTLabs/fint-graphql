// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.basisgruppe;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.elev.BasisgruppeResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("utdanningBasisgruppeService")
public class BasisgruppeService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public BasisgruppeResource getBasisgruppeResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getBasisgruppeResource(
            endpoints.getUtdanningElev() 
                + "/basisgruppe/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public BasisgruppeResource getBasisgruppeResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, BasisgruppeResource.class, dfe);
    }
}

