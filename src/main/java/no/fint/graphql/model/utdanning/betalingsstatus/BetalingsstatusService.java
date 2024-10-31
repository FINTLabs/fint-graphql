
package no.fint.graphql.model.utdanning.betalingsstatus;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.kodeverk.BetalingsstatusResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("utdanningBetalingsstatusService")
public class BetalingsstatusService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<BetalingsstatusResource> getBetalingsstatusResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getBetalingsstatusResource(
            endpoints.getUtdanningKodeverk() 
                + "/betalingsstatus/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<BetalingsstatusResource> getBetalingsstatusResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, BetalingsstatusResource.class, dfe);
    }
}

