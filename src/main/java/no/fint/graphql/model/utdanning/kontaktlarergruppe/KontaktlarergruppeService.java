
package no.fint.graphql.model.utdanning.kontaktlarergruppe;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.elev.KontaktlarergruppeResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("utdanningKontaktlarergruppeService")
public class KontaktlarergruppeService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public KontaktlarergruppeResource getKontaktlarergruppeResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getKontaktlarergruppeResource(
            endpoints.getUtdanningElev() 
                + "/kontaktlarergruppe/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public KontaktlarergruppeResource getKontaktlarergruppeResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, KontaktlarergruppeResource.class, dfe);
    }
}

