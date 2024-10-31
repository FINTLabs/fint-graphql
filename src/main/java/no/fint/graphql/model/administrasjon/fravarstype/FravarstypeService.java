
package no.fint.graphql.model.administrasjon.fravarstype;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.administrasjon.kodeverk.FravarstypeResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("administrasjonFravarstypeService")
public class FravarstypeService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<FravarstypeResource> getFravarstypeResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getFravarstypeResource(
            endpoints.getAdministrasjonKodeverk() 
                + "/fravarstype/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<FravarstypeResource> getFravarstypeResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, FravarstypeResource.class, dfe);
    }
}

