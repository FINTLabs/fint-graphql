
package no.fint.graphql.model.model.eksamensgruppemedlemskap;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.utdanning.vurdering.EksamensgruppemedlemskapResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelEksamensgruppemedlemskapService")
public class EksamensgruppemedlemskapService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<EksamensgruppemedlemskapResource> getEksamensgruppemedlemskapResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getEksamensgruppemedlemskapResource(
            endpoints.getUtdanningVurdering() 
                + "/eksamensgruppemedlemskap/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<EksamensgruppemedlemskapResource> getEksamensgruppemedlemskapResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, EksamensgruppemedlemskapResource.class, dfe);
    }
}

