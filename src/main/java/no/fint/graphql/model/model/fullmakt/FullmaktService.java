
package no.fint.graphql.model.model.fullmakt;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.administrasjon.fullmakt.FullmaktResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelFullmaktService")
public class FullmaktService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<FullmaktResource> getFullmaktResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getFullmaktResource(
            endpoints.getAdministrasjonFullmakt() 
                + "/fullmakt/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<FullmaktResource> getFullmaktResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, FullmaktResource.class, dfe);
    }
}

