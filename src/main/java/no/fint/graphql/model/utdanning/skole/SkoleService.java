
package no.fint.graphql.model.utdanning.skole;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("utdanningSkoleService")
public class SkoleService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public SkoleResource getSkoleResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getSkoleResource(
            endpoints.getUtdanningUtdanningsprogram() 
                + "/skole/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public SkoleResource getSkoleResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, SkoleResource.class, dfe);
    }
}

