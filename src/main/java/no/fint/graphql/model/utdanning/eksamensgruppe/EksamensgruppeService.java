
package no.fint.graphql.model.utdanning.eksamensgruppe;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.vurdering.EksamensgruppeResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("utdanningEksamensgruppeService")
public class EksamensgruppeService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public EksamensgruppeResource getEksamensgruppeResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getEksamensgruppeResource(
            endpoints.getUtdanningVurdering() 
                + "/eksamensgruppe/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public EksamensgruppeResource getEksamensgruppeResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, EksamensgruppeResource.class, dfe);
    }
}

