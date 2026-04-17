
package no.fint.graphql.model.model.fravarsregistrering;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.utdanning.vurdering.FravarsregistreringResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelFravarsregistreringService")
public class FravarsregistreringService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<FravarsregistreringResource> getFravarsregistreringResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getFravarsregistreringResource(
            endpoints.getUtdanningVurdering() 
                + "/fravarsregistrering/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<FravarsregistreringResource> getFravarsregistreringResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, FravarsregistreringResource.class, dfe);
    }
}

