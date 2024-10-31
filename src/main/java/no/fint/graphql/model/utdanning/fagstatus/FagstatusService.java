
package no.fint.graphql.model.utdanning.fagstatus;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.kodeverk.FagstatusResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("utdanningFagstatusService")
public class FagstatusService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<FagstatusResource> getFagstatusResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getFagstatusResource(
            endpoints.getUtdanningKodeverk() 
                + "/fagstatus/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<FagstatusResource> getFagstatusResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, FagstatusResource.class, dfe);
    }
}

