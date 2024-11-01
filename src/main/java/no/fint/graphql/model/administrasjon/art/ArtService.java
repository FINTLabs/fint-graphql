
package no.fint.graphql.model.administrasjon.art;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.administrasjon.kodeverk.ArtResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("administrasjonArtService")
public class ArtService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<ArtResource> getArtResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getArtResource(
            endpoints.getAdministrasjonKodeverk() 
                + "/art/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<ArtResource> getArtResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, ArtResource.class, dfe);
    }
}

