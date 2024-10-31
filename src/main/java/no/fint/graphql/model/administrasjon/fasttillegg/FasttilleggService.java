
package no.fint.graphql.model.administrasjon.fasttillegg;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.administrasjon.personal.FasttilleggResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("administrasjonFasttilleggService")
public class FasttilleggService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<FasttilleggResource> getFasttilleggResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getFasttilleggResource(
            endpoints.getAdministrasjonPersonal() 
                + "/fasttillegg/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<FasttilleggResource> getFasttilleggResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, FasttilleggResource.class, dfe);
    }
}

