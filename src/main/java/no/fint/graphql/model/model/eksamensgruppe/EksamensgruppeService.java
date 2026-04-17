
package no.fint.graphql.model.model.eksamensgruppe;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.utdanning.vurdering.EksamensgruppeResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelEksamensgruppeService")
public class EksamensgruppeService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<EksamensgruppeResource> getEksamensgruppeResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getEksamensgruppeResource(
            endpoints.getUtdanningVurdering() 
                + "/eksamensgruppe/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<EksamensgruppeResource> getEksamensgruppeResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, EksamensgruppeResource.class, dfe);
    }
}

