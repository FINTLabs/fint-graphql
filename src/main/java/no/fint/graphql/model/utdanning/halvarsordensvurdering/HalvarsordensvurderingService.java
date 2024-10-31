
package no.fint.graphql.model.utdanning.halvarsordensvurdering;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.vurdering.HalvarsordensvurderingResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("utdanningHalvarsordensvurderingService")
public class HalvarsordensvurderingService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<HalvarsordensvurderingResource> getHalvarsordensvurderingResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getHalvarsordensvurderingResource(
            endpoints.getUtdanningVurdering() 
                + "/halvarsordensvurdering/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<HalvarsordensvurderingResource> getHalvarsordensvurderingResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, HalvarsordensvurderingResource.class, dfe);
    }
}

