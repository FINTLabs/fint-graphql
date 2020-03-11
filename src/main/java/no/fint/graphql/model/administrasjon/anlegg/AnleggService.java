
package no.fint.graphql.model.administrasjon.anlegg;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.administrasjon.kodeverk.AnleggResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("administrasjonAnleggService")
public class AnleggService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<AnleggResource> getAnleggResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getAnleggResource(
            endpoints.getAdministrasjonKodeverk() 
                + "/anlegg/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<AnleggResource> getAnleggResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, AnleggResource.class, dfe);
    }
}

