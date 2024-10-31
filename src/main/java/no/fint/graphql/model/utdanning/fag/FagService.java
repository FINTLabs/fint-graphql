
package no.fint.graphql.model.utdanning.fag;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.timeplan.FagResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("utdanningFagService")
public class FagService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<FagResource> getFagResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getFagResource(
            endpoints.getUtdanningTimeplan() 
                + "/fag/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<FagResource> getFagResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, FagResource.class, dfe);
    }
}

