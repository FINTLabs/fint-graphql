
package no.fint.graphql.model.model.lopenummer;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.administrasjon.kodeverk.LopenummerResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelLopenummerService")
public class LopenummerService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<LopenummerResource> getLopenummerResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getLopenummerResource(
            endpoints.getAdministrasjonKodeverk() 
                + "/lopenummer/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<LopenummerResource> getLopenummerResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, LopenummerResource.class, dfe);
    }
}

