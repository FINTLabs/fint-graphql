
package no.fint.graphql.model.model.elev;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.utdanning.elev.ElevResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelElevService")
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

