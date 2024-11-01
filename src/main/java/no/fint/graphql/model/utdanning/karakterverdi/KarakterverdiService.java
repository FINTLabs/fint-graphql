
package no.fint.graphql.model.utdanning.karakterverdi;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.vurdering.KarakterverdiResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("utdanningKarakterverdiService")
public class KarakterverdiService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<KarakterverdiResource> getKarakterverdiResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getKarakterverdiResource(
            endpoints.getUtdanningVurdering() 
                + "/karakterverdi/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<KarakterverdiResource> getKarakterverdiResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, KarakterverdiResource.class, dfe);
    }
}

