
package no.fint.graphql.model.utdanning.elevforhold;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.elev.ElevforholdResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("utdanningElevforholdService")
public class ElevforholdService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<ElevforholdResource> getElevforholdResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getElevforholdResource(
            endpoints.getUtdanningElev() 
                + "/elevforhold/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<ElevforholdResource> getElevforholdResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, ElevforholdResource.class, dfe);
    }
}

