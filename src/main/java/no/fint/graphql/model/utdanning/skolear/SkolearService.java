
package no.fint.graphql.model.utdanning.skolear;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.kodeverk.SkolearResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("utdanningSkolearService")
public class SkolearService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<SkolearResource> getSkolearResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getSkolearResource(
            endpoints.getUtdanningKodeverk() 
                + "/skolear/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<SkolearResource> getSkolearResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, SkolearResource.class, dfe);
    }
}

