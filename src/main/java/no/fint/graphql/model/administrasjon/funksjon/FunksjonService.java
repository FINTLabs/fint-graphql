
package no.fint.graphql.model.administrasjon.funksjon;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.administrasjon.kodeverk.FunksjonResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("administrasjonFunksjonService")
public class FunksjonService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<FunksjonResource> getFunksjonResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getFunksjonResource(
            endpoints.getAdministrasjonKodeverk() 
                + "/funksjon/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<FunksjonResource> getFunksjonResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, FunksjonResource.class, dfe);
    }
}

