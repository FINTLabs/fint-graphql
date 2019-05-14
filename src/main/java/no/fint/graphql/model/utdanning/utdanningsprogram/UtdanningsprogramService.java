
package no.fint.graphql.model.utdanning.utdanningsprogram;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.utdanningsprogram.UtdanningsprogramResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("utdanningUtdanningsprogramService")
public class UtdanningsprogramService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public UtdanningsprogramResource getUtdanningsprogramResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getUtdanningsprogramResource(
            endpoints.getUtdanningUtdanningsprogram() 
                + "/utdanningsprogram/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public UtdanningsprogramResource getUtdanningsprogramResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, UtdanningsprogramResource.class, dfe);
    }
}

