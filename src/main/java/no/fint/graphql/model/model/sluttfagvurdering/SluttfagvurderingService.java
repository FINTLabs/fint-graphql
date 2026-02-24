
package no.fint.graphql.model.model.sluttfagvurdering;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.utdanning.vurdering.SluttfagvurderingResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelSluttfagvurderingService")
public class SluttfagvurderingService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<SluttfagvurderingResource> getSluttfagvurderingResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getSluttfagvurderingResource(
            endpoints.getUtdanningVurdering() 
                + "/sluttfagvurdering/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<SluttfagvurderingResource> getSluttfagvurderingResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, SluttfagvurderingResource.class, dfe);
    }
}

