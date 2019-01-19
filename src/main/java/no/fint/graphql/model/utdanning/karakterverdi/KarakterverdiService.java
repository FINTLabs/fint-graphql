// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.karakterverdi;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.vurdering.KarakterverdiResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("utdanningKarakterverdiService")
public class KarakterverdiService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public KarakterverdiResource getKarakterverdiResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getKarakterverdiResource(
            endpoints.getUtdanningVurdering() 
                + "/karakterverdi/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public KarakterverdiResource getKarakterverdiResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, KarakterverdiResource.class, dfe);
    }
}

