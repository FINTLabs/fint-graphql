
package no.fint.graphql.model.model.uketimetall;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.administrasjon.kodeverk.UketimetallResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelUketimetallService")
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

