
package no.fint.graphql.model.utdanning.termin;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.kodeverk.TerminResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("utdanningTerminService")
public class TerminService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<TerminResource> getTerminResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getTerminResource(
            endpoints.getUtdanningKodeverk() 
                + "/termin/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<TerminResource> getTerminResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, TerminResource.class, dfe);
    }
}

