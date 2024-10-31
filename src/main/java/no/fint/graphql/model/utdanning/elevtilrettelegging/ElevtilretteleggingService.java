
package no.fint.graphql.model.utdanning.elevtilrettelegging;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.elev.ElevtilretteleggingResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("utdanningElevtilretteleggingService")
public class ElevtilretteleggingService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<ElevtilretteleggingResource> getElevtilretteleggingResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getElevtilretteleggingResource(
            endpoints.getUtdanningElev() 
                + "/elevtilrettelegging/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<ElevtilretteleggingResource> getElevtilretteleggingResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, ElevtilretteleggingResource.class, dfe);
    }
}

