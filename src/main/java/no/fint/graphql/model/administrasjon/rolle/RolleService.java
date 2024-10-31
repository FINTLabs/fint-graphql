
package no.fint.graphql.model.administrasjon.rolle;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.administrasjon.fullmakt.RolleResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("administrasjonRolleService")
public class RolleService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<RolleResource> getRolleResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getRolleResource(
            endpoints.getAdministrasjonFullmakt() 
                + "/rolle/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<RolleResource> getRolleResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, RolleResource.class, dfe);
    }
}

