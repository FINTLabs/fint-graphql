
package no.fint.graphql.model.model.elevvurdering;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.utdanning.vurdering.ElevvurderingResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelElevvurderingService")
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

