
package no.fint.graphql.model.felles.fylke;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.felles.kodeverk.FylkeResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("fellesFylkeService")
public class FylkeService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<FylkeResource> getFylkeResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getFylkeResource(
            endpoints.getFellesKodeverk() 
                + "/fylke/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<FylkeResource> getFylkeResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, FylkeResource.class, dfe);
    }
}

