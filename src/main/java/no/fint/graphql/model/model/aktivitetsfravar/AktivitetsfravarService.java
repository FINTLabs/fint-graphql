
package no.fint.graphql.model.model.aktivitetsfravar;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.utdanning.vurdering.AktivitetsfravarResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelAktivitetsfravarService")
public class AktivitetsfravarService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<AktivitetsfravarResource> getAktivitetsfravarResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getAktivitetsfravarResource(
            endpoints.getUtdanningVurdering() 
                + "/aktivitetsfravar/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<AktivitetsfravarResource> getAktivitetsfravarResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, AktivitetsfravarResource.class, dfe);
    }
}

