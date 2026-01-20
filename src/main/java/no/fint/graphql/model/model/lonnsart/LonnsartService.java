
package no.fint.graphql.model.model.lonnsart;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.administrasjon.kodeverk.LonnsartResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelLonnsartService")
public class LonnsartService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<LonnsartResource> getLonnsartResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getLonnsartResource(
            endpoints.getAdministrasjonKodeverk() 
                + "/lonnsart/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<LonnsartResource> getLonnsartResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, LonnsartResource.class, dfe);
    }
}

