
package no.fint.graphql.model.administrasjon.prosjekt;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.administrasjon.kodeverk.ProsjektResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("administrasjonProsjektService")
public class ProsjektService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<ProsjektResource> getProsjektResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getProsjektResource(
            endpoints.getAdministrasjonKodeverk() 
                + "/prosjekt/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<ProsjektResource> getProsjektResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, ProsjektResource.class, dfe);
    }
}

