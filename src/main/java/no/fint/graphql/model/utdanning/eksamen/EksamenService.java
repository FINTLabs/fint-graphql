
package no.fint.graphql.model.utdanning.eksamen;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.timeplan.EksamenResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("utdanningEksamenService")
public class EksamenService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<EksamenResource> getEksamenResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getEksamenResource(
            endpoints.getUtdanningTimeplan() 
                + "/eksamen/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<EksamenResource> getEksamenResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, EksamenResource.class, dfe);
    }
}

