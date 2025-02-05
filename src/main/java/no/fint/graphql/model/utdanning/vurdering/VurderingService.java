
package no.fint.graphql.model.utdanning.vurdering;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.vurdering.VurderingResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("utdanningVurderingService")
public class VurderingService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<VurderingResource> getVurderingResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getVurderingResource(
            endpoints.getUtdanningVurdering() 
                + "/vurdering/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<VurderingResource> getVurderingResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, VurderingResource.class, dfe);
    }
}

