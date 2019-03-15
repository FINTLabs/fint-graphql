// Built from tag release-3.2

package no.fint.graphql.model.utdanning.time;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.timeplan.TimeResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("utdanningTimeService")
public class TimeService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public TimeResource getTimeResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getTimeResource(
            endpoints.getUtdanningTimeplan() 
                + "/time/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public TimeResource getTimeResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, TimeResource.class, dfe);
    }
}

