
package no.fint.graphql.model.model.klasse;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.utdanning.elev.KlasseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelKlasseService")
public class KlasseService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<KlasseResource> getKlasseResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getKlasseResource(
            endpoints.getUtdanningElev() 
                + "/klasse/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<KlasseResource> getKlasseResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, KlasseResource.class, dfe);
    }
}

