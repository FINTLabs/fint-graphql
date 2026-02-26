
package no.fint.graphql.model.model.karakterhistorie;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.utdanning.vurdering.KarakterhistorieResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelKarakterhistorieService")
public class KarakterhistorieService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<KarakterhistorieResource> getKarakterhistorieResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getKarakterhistorieResource(
            endpoints.getUtdanningVurdering() 
                + "/karakterhistorie/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<KarakterhistorieResource> getKarakterhistorieResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, KarakterhistorieResource.class, dfe);
    }
}

