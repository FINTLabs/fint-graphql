
package no.fint.graphql.model.model.fagmerknad;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.utdanning.kodeverk.FagmerknadResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelFagmerknadService")
public class FagmerknadService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<FagmerknadResource> getFagmerknadResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getFagmerknadResource(
            endpoints.getUtdanningKodeverk() 
                + "/fagmerknad/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<FagmerknadResource> getFagmerknadResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, FagmerknadResource.class, dfe);
    }
}

