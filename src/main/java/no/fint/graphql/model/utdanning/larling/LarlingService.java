
package no.fint.graphql.model.utdanning.larling;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.larling.LarlingResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("utdanningLarlingService")
public class LarlingService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<LarlingResource> getLarlingResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getLarlingResource(
            endpoints.getUtdanningLarling() 
                + "/larling/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<LarlingResource> getLarlingResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, LarlingResource.class, dfe);
    }
}

