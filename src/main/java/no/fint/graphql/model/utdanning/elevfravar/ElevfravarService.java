
package no.fint.graphql.model.utdanning.elevfravar;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.vurdering.ElevfravarResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("utdanningElevfravarService")
public class ElevfravarService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<ElevfravarResource> getElevfravarResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getElevfravarResource(
            endpoints.getUtdanningVurdering() 
                + "/elevfravar/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<ElevfravarResource> getElevfravarResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, ElevfravarResource.class, dfe);
    }
}

