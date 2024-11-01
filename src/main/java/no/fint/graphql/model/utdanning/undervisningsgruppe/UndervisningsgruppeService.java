
package no.fint.graphql.model.utdanning.undervisningsgruppe;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("utdanningUndervisningsgruppeService")
public class UndervisningsgruppeService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<UndervisningsgruppeResource> getUndervisningsgruppeResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getUndervisningsgruppeResource(
            endpoints.getUtdanningTimeplan() 
                + "/undervisningsgruppe/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<UndervisningsgruppeResource> getUndervisningsgruppeResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, UndervisningsgruppeResource.class, dfe);
    }
}

