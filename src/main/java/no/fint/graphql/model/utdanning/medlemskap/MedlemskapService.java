
package no.fint.graphql.model.utdanning.medlemskap;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.elev.MedlemskapResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("utdanningMedlemskapService")
public class MedlemskapService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<MedlemskapResource> getMedlemskapResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getMedlemskapResource(
            endpoints.getUtdanningElev() 
                + "/medlemskap/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<MedlemskapResource> getMedlemskapResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, MedlemskapResource.class, dfe);
    }
}

