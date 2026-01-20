
package no.fint.graphql.model.model.fullfortkode;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.utdanning.kodeverk.FullfortkodeResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelFullfortkodeService")
public class FullfortkodeService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<FullfortkodeResource> getFullfortkodeResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getFullfortkodeResource(
            endpoints.getUtdanningKodeverk() 
                + "/fullfortkode/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<FullfortkodeResource> getFullfortkodeResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, FullfortkodeResource.class, dfe);
    }
}

