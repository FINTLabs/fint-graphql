
package no.fint.graphql.model.administrasjon.uketimetall;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.administrasjon.kodeverk.UketimetallResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("administrasjonUketimetallService")
public class UketimetallService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<UketimetallResource> getUketimetallResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getUketimetallResource(
            endpoints.getAdministrasjonKodeverk() 
                + "/uketimetall/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<UketimetallResource> getUketimetallResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, UketimetallResource.class, dfe);
    }
}

