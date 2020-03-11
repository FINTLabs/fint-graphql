
package no.fint.graphql.model.felles.landkode;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.felles.kodeverk.iso.LandkodeResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("fellesLandkodeService")
public class LandkodeService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<LandkodeResource> getLandkodeResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getLandkodeResource(
            endpoints.getFellesKodeverkIso() 
                + "/landkode/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<LandkodeResource> getLandkodeResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, LandkodeResource.class, dfe);
    }
}

