
package no.fint.graphql.model.utdanning.faggruppe;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.timeplan.FaggruppeResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("utdanningFaggruppeService")
public class FaggruppeService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<FaggruppeResource> getFaggruppeResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getFaggruppeResource(
            endpoints.getUtdanningTimeplan() 
                + "/faggruppe/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<FaggruppeResource> getFaggruppeResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, FaggruppeResource.class, dfe);
    }
}

