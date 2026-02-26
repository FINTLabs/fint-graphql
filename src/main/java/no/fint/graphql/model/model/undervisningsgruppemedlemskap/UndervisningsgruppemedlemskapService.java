
package no.fint.graphql.model.model.undervisningsgruppemedlemskap;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.utdanning.timeplan.UndervisningsgruppemedlemskapResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelUndervisningsgruppemedlemskapService")
public class UndervisningsgruppemedlemskapService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<UndervisningsgruppemedlemskapResource> getUndervisningsgruppemedlemskapResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getUndervisningsgruppemedlemskapResource(
            endpoints.getUtdanningTimeplan() 
                + "/undervisningsgruppemedlemskap/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<UndervisningsgruppemedlemskapResource> getUndervisningsgruppemedlemskapResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, UndervisningsgruppemedlemskapResource.class, dfe);
    }
}

