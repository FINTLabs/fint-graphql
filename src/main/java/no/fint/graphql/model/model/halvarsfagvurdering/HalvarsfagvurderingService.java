
package no.fint.graphql.model.model.halvarsfagvurdering;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.utdanning.vurdering.HalvarsfagvurderingResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelHalvarsfagvurderingService")
public class HalvarsfagvurderingService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<HalvarsfagvurderingResource> getHalvarsfagvurderingResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getHalvarsfagvurderingResource(
            endpoints.getUtdanningVurdering() 
                + "/halvarsfagvurdering/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<HalvarsfagvurderingResource> getHalvarsfagvurderingResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, HalvarsfagvurderingResource.class, dfe);
    }
}

