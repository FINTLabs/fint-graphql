
package no.fint.graphql.model.felles.virksomhet;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.felles.VirksomhetResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("fellesVirksomhetService")
public class VirksomhetService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<VirksomhetResource> getVirksomhetResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getVirksomhetResource(
            endpoints.getFelles() 
                + "/virksomhet/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<VirksomhetResource> getVirksomhetResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, VirksomhetResource.class, dfe);
    }
}

