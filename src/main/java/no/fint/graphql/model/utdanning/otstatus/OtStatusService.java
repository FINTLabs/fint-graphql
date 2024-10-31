
package no.fint.graphql.model.utdanning.otstatus;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.kodeverk.OtStatusResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("utdanningOtStatusService")
public class OtStatusService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<OtStatusResource> getOtStatusResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getOtStatusResource(
            endpoints.getUtdanningKodeverk() 
                + "/otstatus/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<OtStatusResource> getOtStatusResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, OtStatusResource.class, dfe);
    }
}

