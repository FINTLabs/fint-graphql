
package no.fint.graphql.model.model.tilrettelegging;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.utdanning.kodeverk.TilretteleggingResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelTilretteleggingService")
public class TilretteleggingService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<TilretteleggingResource> getTilretteleggingResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getTilretteleggingResource(
            endpoints.getUtdanningKodeverk() 
                + "/tilrettelegging/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<TilretteleggingResource> getTilretteleggingResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, TilretteleggingResource.class, dfe);
    }
}

