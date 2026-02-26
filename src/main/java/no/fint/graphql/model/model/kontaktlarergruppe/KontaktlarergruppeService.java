
package no.fint.graphql.model.model.kontaktlarergruppe;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.utdanning.elev.KontaktlarergruppeResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelKontaktlarergruppeService")
public class KontaktlarergruppeService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<KontaktlarergruppeResource> getKontaktlarergruppeResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getKontaktlarergruppeResource(
            endpoints.getUtdanningElev() 
                + "/kontaktlarergruppe/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<KontaktlarergruppeResource> getKontaktlarergruppeResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, KontaktlarergruppeResource.class, dfe);
    }
}

