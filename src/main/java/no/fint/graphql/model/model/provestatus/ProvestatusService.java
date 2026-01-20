
package no.fint.graphql.model.model.provestatus;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.utdanning.kodeverk.ProvestatusResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelProvestatusService")
public class ProvestatusService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<ProvestatusResource> getProvestatusResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getProvestatusResource(
            endpoints.getUtdanningKodeverk() 
                + "/provestatus/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<ProvestatusResource> getProvestatusResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, ProvestatusResource.class, dfe);
    }
}

