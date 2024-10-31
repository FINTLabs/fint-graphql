
package no.fint.graphql.model.utdanning.varseltype;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.kodeverk.VarseltypeResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("utdanningVarseltypeService")
public class VarseltypeService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<VarseltypeResource> getVarseltypeResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getVarseltypeResource(
            endpoints.getUtdanningKodeverk() 
                + "/varseltype/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<VarseltypeResource> getVarseltypeResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, VarseltypeResource.class, dfe);
    }
}

