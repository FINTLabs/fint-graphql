
package no.fint.graphql.model.utdanning.eksamensvurdering;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.vurdering.EksamensvurderingResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("utdanningEksamensvurderingService")
public class EksamensvurderingService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<EksamensvurderingResource> getEksamensvurderingResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getEksamensvurderingResource(
            endpoints.getUtdanningVurdering() 
                + "/eksamensvurdering/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<EksamensvurderingResource> getEksamensvurderingResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, EksamensvurderingResource.class, dfe);
    }
}

