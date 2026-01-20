
package no.fint.graphql.model.model.anmerkninger;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.utdanning.vurdering.AnmerkningerResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelAnmerkningerService")
public class AnmerkningerService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<AnmerkningerResource> getAnmerkningerResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getAnmerkningerResource(
            endpoints.getUtdanningVurdering() 
                + "/anmerkninger/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<AnmerkningerResource> getAnmerkningerResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, AnmerkningerResource.class, dfe);
    }
}

