
package no.fint.graphql.model.administrasjon.diverse;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.administrasjon.kodeverk.DiverseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("administrasjonDiverseService")
public class DiverseService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<DiverseResource> getDiverseResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getDiverseResource(
            endpoints.getAdministrasjonKodeverk() 
                + "/diverse/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<DiverseResource> getDiverseResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, DiverseResource.class, dfe);
    }
}

