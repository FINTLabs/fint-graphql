
package no.fint.graphql.model.model.karakterstatus;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.utdanning.kodeverk.KarakterstatusResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelKarakterstatusService")
public class KarakterstatusService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<KarakterstatusResource> getKarakterstatusResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getKarakterstatusResource(
            endpoints.getUtdanningKodeverk() 
                + "/karakterstatus/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<KarakterstatusResource> getKarakterstatusResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, KarakterstatusResource.class, dfe);
    }
}

