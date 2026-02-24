
package no.fint.graphql.model.model.vitnemalsmerknad;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.utdanning.kodeverk.VitnemalsmerknadResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelVitnemalsmerknadService")
public class VitnemalsmerknadService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<VitnemalsmerknadResource> getVitnemalsmerknadResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getVitnemalsmerknadResource(
            endpoints.getUtdanningKodeverk() 
                + "/vitnemalsmerknad/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<VitnemalsmerknadResource> getVitnemalsmerknadResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, VitnemalsmerknadResource.class, dfe);
    }
}

