
package no.fint.graphql.model.administrasjon.kontrakt;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.administrasjon.kodeverk.KontraktResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("administrasjonKontraktService")
public class KontraktService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<KontraktResource> getKontraktResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getKontraktResource(
            endpoints.getAdministrasjonKodeverk() 
                + "/kontrakt/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<KontraktResource> getKontraktResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, KontraktResource.class, dfe);
    }
}

