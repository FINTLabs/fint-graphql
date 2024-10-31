
package no.fint.graphql.model.utdanning.bevistype;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.kodeverk.BevistypeResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("utdanningBevistypeService")
public class BevistypeService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<BevistypeResource> getBevistypeResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getBevistypeResource(
            endpoints.getUtdanningKodeverk() 
                + "/bevistype/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<BevistypeResource> getBevistypeResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, BevistypeResource.class, dfe);
    }
}

