
package no.fint.graphql.model.utdanning.basisgruppe;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.elev.BasisgruppeResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("utdanningBasisgruppeService")
public class BasisgruppeService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<BasisgruppeResource> getBasisgruppeResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getBasisgruppeResource(
            endpoints.getUtdanningElev() 
                + "/basisgruppe/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<BasisgruppeResource> getBasisgruppeResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, BasisgruppeResource.class, dfe);
    }
}

