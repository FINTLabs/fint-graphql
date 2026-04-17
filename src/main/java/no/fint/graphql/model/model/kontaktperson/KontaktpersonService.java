
package no.fint.graphql.model.model.kontaktperson;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.felles.KontaktpersonResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelKontaktpersonService")
public class KontaktpersonService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<KontaktpersonResource> getKontaktpersonResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getKontaktpersonResource(
            endpoints.getFelles() 
                + "/kontaktperson/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<KontaktpersonResource> getKontaktpersonResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, KontaktpersonResource.class, dfe);
    }
}

