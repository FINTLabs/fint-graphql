
package no.fint.graphql.model.model.fastlonn;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.administrasjon.personal.FastlonnResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelFastlonnService")
public class FastlonnService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<FastlonnResource> getFastlonnResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getFastlonnResource(
            endpoints.getAdministrasjonPersonal() 
                + "/fastlonn/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<FastlonnResource> getFastlonnResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, FastlonnResource.class, dfe);
    }
}

