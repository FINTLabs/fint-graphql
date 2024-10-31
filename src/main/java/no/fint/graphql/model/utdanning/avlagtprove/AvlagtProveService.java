
package no.fint.graphql.model.utdanning.avlagtprove;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.larling.AvlagtProveResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("utdanningAvlagtProveService")
public class AvlagtProveService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<AvlagtProveResource> getAvlagtProveResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getAvlagtProveResource(
            endpoints.getUtdanningLarling() 
                + "/avlagtprove/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<AvlagtProveResource> getAvlagtProveResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, AvlagtProveResource.class, dfe);
    }
}

