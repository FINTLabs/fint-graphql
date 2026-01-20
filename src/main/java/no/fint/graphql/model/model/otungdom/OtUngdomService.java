
package no.fint.graphql.model.model.otungdom;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.utdanning.ot.OtUngdomResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelOtUngdomService")
public class OtUngdomService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<OtUngdomResource> getOtUngdomResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getOtUngdomResource(
            endpoints.getUtdanningOt() 
                + "/otungdom/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<OtUngdomResource> getOtUngdomResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, OtUngdomResource.class, dfe);
    }
}

