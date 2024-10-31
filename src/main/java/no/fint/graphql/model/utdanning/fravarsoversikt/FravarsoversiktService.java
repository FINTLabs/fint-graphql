
package no.fint.graphql.model.utdanning.fravarsoversikt;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.vurdering.FravarsoversiktResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("utdanningFravarsoversiktService")
public class FravarsoversiktService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<FravarsoversiktResource> getFravarsoversiktResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getFravarsoversiktResource(
            endpoints.getUtdanningVurdering() 
                + "/fravarsoversikt/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<FravarsoversiktResource> getFravarsoversiktResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, FravarsoversiktResource.class, dfe);
    }
}

