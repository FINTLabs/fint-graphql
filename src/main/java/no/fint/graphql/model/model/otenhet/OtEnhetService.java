
package no.fint.graphql.model.model.otenhet;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.utdanning.kodeverk.OtEnhetResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelOtEnhetService")
public class OtEnhetService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<OtEnhetResource> getOtEnhetResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getOtEnhetResource(
            endpoints.getUtdanningKodeverk() 
                + "/otenhet/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<OtEnhetResource> getOtEnhetResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, OtEnhetResource.class, dfe);
    }
}

