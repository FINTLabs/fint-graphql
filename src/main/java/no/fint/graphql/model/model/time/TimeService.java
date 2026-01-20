
package no.fint.graphql.model.model.time;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.utdanning.timeplan.TimeResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelTimeService")
public class TimeService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<TimeResource> getTimeResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getTimeResource(
            endpoints.getUtdanningTimeplan() 
                + "/time/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<TimeResource> getTimeResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, TimeResource.class, dfe);
    }
}

