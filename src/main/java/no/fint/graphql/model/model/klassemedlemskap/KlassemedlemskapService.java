
package no.fint.graphql.model.model.klassemedlemskap;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.utdanning.elev.KlassemedlemskapResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelKlassemedlemskapService")
public class KlassemedlemskapService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<KlassemedlemskapResource> getKlassemedlemskapResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getKlassemedlemskapResource(
            endpoints.getUtdanningElev() 
                + "/klassemedlemskap/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<KlassemedlemskapResource> getKlassemedlemskapResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, KlassemedlemskapResource.class, dfe);
    }
}

