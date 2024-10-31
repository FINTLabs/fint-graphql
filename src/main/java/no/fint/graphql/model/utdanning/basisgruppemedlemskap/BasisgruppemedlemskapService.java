
package no.fint.graphql.model.utdanning.basisgruppemedlemskap;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.elev.BasisgruppemedlemskapResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("utdanningBasisgruppemedlemskapService")
public class BasisgruppemedlemskapService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<BasisgruppemedlemskapResource> getBasisgruppemedlemskapResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getBasisgruppemedlemskapResource(
            endpoints.getUtdanningElev() 
                + "/basisgruppemedlemskap/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<BasisgruppemedlemskapResource> getBasisgruppemedlemskapResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, BasisgruppemedlemskapResource.class, dfe);
    }
}

