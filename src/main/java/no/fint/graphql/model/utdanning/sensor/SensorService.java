
package no.fint.graphql.model.utdanning.sensor;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.vurdering.SensorResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("utdanningSensorService")
public class SensorService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<SensorResource> getSensorResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getSensorResource(
            endpoints.getUtdanningVurdering() 
                + "/sensor/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<SensorResource> getSensorResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, SensorResource.class, dfe);
    }
}

