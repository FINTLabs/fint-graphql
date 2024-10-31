
package no.fint.graphql.model.utdanning.persongruppemedlemskap;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.elev.PersongruppemedlemskapResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("utdanningPersongruppemedlemskapService")
public class PersongruppemedlemskapService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<PersongruppemedlemskapResource> getPersongruppemedlemskapResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getPersongruppemedlemskapResource(
            endpoints.getUtdanningElev() 
                + "/persongruppemedlemskap/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<PersongruppemedlemskapResource> getPersongruppemedlemskapResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, PersongruppemedlemskapResource.class, dfe);
    }
}

