
package no.fint.graphql.model.utdanning.sluttordensvurdering;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.vurdering.SluttordensvurderingResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("utdanningSluttordensvurderingService")
public class SluttordensvurderingService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<SluttordensvurderingResource> getSluttordensvurderingResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getSluttordensvurderingResource(
            endpoints.getUtdanningVurdering() 
                + "/sluttordensvurdering/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<SluttordensvurderingResource> getSluttordensvurderingResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, SluttordensvurderingResource.class, dfe);
    }
}

