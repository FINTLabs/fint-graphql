
package no.fint.graphql.model.model.faggruppemedlemskap;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.utdanning.timeplan.FaggruppemedlemskapResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelFaggruppemedlemskapService")
public class FaggruppemedlemskapService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<FaggruppemedlemskapResource> getFaggruppemedlemskapResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getFaggruppemedlemskapResource(
            endpoints.getUtdanningTimeplan() 
                + "/faggruppemedlemskap/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<FaggruppemedlemskapResource> getFaggruppemedlemskapResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, FaggruppemedlemskapResource.class, dfe);
    }
}

