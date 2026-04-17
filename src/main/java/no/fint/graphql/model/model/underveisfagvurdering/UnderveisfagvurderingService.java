
package no.fint.graphql.model.model.underveisfagvurdering;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.utdanning.vurdering.UnderveisfagvurderingResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelUnderveisfagvurderingService")
public class UnderveisfagvurderingService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<UnderveisfagvurderingResource> getUnderveisfagvurderingResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getUnderveisfagvurderingResource(
            endpoints.getUtdanningVurdering() 
                + "/underveisfagvurdering/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<UnderveisfagvurderingResource> getUnderveisfagvurderingResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, UnderveisfagvurderingResource.class, dfe);
    }
}

