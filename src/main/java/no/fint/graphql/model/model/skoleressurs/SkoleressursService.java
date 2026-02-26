
package no.fint.graphql.model.model.skoleressurs;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.utdanning.elev.SkoleressursResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelSkoleressursService")
public class SkoleressursService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<SkoleressursResource> getSkoleressursResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getSkoleressursResource(
            endpoints.getUtdanningElev() 
                + "/skoleressurs/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<SkoleressursResource> getSkoleressursResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, SkoleressursResource.class, dfe);
    }
}

