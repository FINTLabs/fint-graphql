// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.rom;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.timeplan.RomResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("utdanningRomService")
public class RomService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public RomResource getRomResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getRomResource(
            endpoints.getUtdanningTimeplan() 
                + "/rom/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public RomResource getRomResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, RomResource.class, dfe);
    }
}

