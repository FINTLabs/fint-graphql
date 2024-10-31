
package no.fint.graphql.model.utdanning.arstrinn;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.utdanningsprogram.ArstrinnResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("utdanningArstrinnService")
public class ArstrinnService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<ArstrinnResource> getArstrinnResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getArstrinnResource(
            endpoints.getUtdanningUtdanningsprogram() 
                + "/arstrinn/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<ArstrinnResource> getArstrinnResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, ArstrinnResource.class, dfe);
    }
}

