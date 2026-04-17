
package no.fint.graphql.model.model.elevkategori;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.utdanning.kodeverk.ElevkategoriResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelElevkategoriService")
public class ElevkategoriService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<ElevkategoriResource> getElevkategoriResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getElevkategoriResource(
            endpoints.getUtdanningKodeverk() 
                + "/elevkategori/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<ElevkategoriResource> getElevkategoriResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, ElevkategoriResource.class, dfe);
    }
}

