
package no.fint.graphql.model.utdanning.elevvurdering;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.vurdering.ElevvurderingResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("utdanningElevvurderingService")
public class ElevvurderingService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<ElevvurderingResource> getElevvurderingResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getElevvurderingResource(
            endpoints.getUtdanningVurdering() 
                + "/elevvurdering/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<ElevvurderingResource> getElevvurderingResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, ElevvurderingResource.class, dfe);
    }
}

