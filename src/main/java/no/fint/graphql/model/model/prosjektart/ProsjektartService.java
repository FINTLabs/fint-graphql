
package no.fint.graphql.model.model.prosjektart;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.administrasjon.kodeverk.ProsjektartResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelProsjektartService")
public class ProsjektartService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<ProsjektartResource> getProsjektartResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getProsjektartResource(
            endpoints.getAdministrasjonKodeverk() 
                + "/prosjektart/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<ProsjektartResource> getProsjektartResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, ProsjektartResource.class, dfe);
    }
}

