
package model.Elev;

import graphql.schema.DataFetchingEnvironment;
import model.Endpoints;
import no.fint.graphql.WebClientRequest;
import no.fint.model.resource.utdanning.elev.ElevResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("utdanningElevService")
public class ElevService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<ElevResource> getElevResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getElevResource(
            endpoints.getUtdanningElev() 
                + "/elev/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<ElevResource> getElevResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, ElevResource.class, dfe);
    }
}

