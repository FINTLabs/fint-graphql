
package no.fint.graphql.model.model.rom;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.utdanning.timeplan.RomResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelRomService")
public class RomService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<RomResource> getRomResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getRomResource(
            endpoints.getUtdanningTimeplan() 
                + "/rom/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<RomResource> getRomResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, RomResource.class, dfe);
    }
}

