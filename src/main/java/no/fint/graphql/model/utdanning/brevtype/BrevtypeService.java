
package no.fint.graphql.model.utdanning.brevtype;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.kodeverk.BrevtypeResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("utdanningBrevtypeService")
public class BrevtypeService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<BrevtypeResource> getBrevtypeResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getBrevtypeResource(
            endpoints.getUtdanningKodeverk() 
                + "/brevtype/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<BrevtypeResource> getBrevtypeResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, BrevtypeResource.class, dfe);
    }
}

