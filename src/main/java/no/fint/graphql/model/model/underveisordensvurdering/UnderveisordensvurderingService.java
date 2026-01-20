
package no.fint.graphql.model.model.underveisordensvurdering;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.utdanning.vurdering.UnderveisordensvurderingResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelUnderveisordensvurderingService")
public class UnderveisordensvurderingService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<UnderveisordensvurderingResource> getUnderveisordensvurderingResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getUnderveisordensvurderingResource(
            endpoints.getUtdanningVurdering() 
                + "/underveisordensvurdering/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<UnderveisordensvurderingResource> getUnderveisordensvurderingResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, UnderveisordensvurderingResource.class, dfe);
    }
}

