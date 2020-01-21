
package no.fint.graphql.model.utdanning.skoleeiertype;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.kodeverk.SkoleeiertypeResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("utdanningSkoleeiertypeService")
public class SkoleeiertypeService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<SkoleeiertypeResource> getSkoleeiertypeResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getSkoleeiertypeResource(
            endpoints.getUtdanningKodeverk() 
                + "/skoleeiertype/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<SkoleeiertypeResource> getSkoleeiertypeResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, SkoleeiertypeResource.class, dfe);
    }
}

