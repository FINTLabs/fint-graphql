
package no.fint.graphql.model.model.fravarsgrunn;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.administrasjon.kodeverk.FravarsgrunnResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelFravarsgrunnService")
public class FravarsgrunnService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<FravarsgrunnResource> getFravarsgrunnResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getFravarsgrunnResource(
            endpoints.getAdministrasjonKodeverk() 
                + "/fravarsgrunn/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<FravarsgrunnResource> getFravarsgrunnResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, FravarsgrunnResource.class, dfe);
    }
}

