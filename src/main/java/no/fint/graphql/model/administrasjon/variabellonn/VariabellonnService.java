
package no.fint.graphql.model.administrasjon.variabellonn;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.administrasjon.personal.VariabellonnResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("administrasjonVariabellonnService")
public class VariabellonnService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<VariabellonnResource> getVariabellonnResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getVariabellonnResource(
            endpoints.getAdministrasjonPersonal() 
                + "/variabellonn/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<VariabellonnResource> getVariabellonnResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, VariabellonnResource.class, dfe);
    }
}

