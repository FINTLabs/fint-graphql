
package no.fint.graphql.model.administrasjon.ramme;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.administrasjon.kodeverk.RammeResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("administrasjonRammeService")
public class RammeService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<RammeResource> getRammeResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getRammeResource(
            endpoints.getAdministrasjonKodeverk() 
                + "/ramme/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<RammeResource> getRammeResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, RammeResource.class, dfe);
    }
}

