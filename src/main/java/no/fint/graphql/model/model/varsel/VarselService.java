
package no.fint.graphql.model.model.varsel;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.utdanning.elev.VarselResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelVarselService")
public class VarselService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<VarselResource> getVarselResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getVarselResource(
            endpoints.getUtdanningElev() 
                + "/varsel/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<VarselResource> getVarselResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, VarselResource.class, dfe);
    }
}

