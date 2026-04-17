
package no.fint.graphql.model.model.formal;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.administrasjon.kodeverk.FormalResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelFormalService")
public class FormalService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<FormalResource> getFormalResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getFormalResource(
            endpoints.getAdministrasjonKodeverk() 
                + "/formal/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<FormalResource> getFormalResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, FormalResource.class, dfe);
    }
}

