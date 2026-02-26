
package no.fint.graphql.model.model.karakterskala;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.utdanning.kodeverk.KarakterskalaResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelKarakterskalaService")
public class KarakterskalaService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<KarakterskalaResource> getKarakterskalaResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getKarakterskalaResource(
            endpoints.getUtdanningKodeverk() 
                + "/karakterskala/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<KarakterskalaResource> getKarakterskalaResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, KarakterskalaResource.class, dfe);
    }
}

