
package no.fint.graphql.model.utdanning.avbruddsarsak;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.kodeverk.AvbruddsarsakResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("utdanningAvbruddsarsakService")
public class AvbruddsarsakService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<AvbruddsarsakResource> getAvbruddsarsakResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getAvbruddsarsakResource(
            endpoints.getUtdanningKodeverk() 
                + "/avbruddsarsak/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<AvbruddsarsakResource> getAvbruddsarsakResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, AvbruddsarsakResource.class, dfe);
    }
}

