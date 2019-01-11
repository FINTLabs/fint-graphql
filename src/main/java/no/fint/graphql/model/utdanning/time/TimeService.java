// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.time;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.timeplan.TimeResource;
import no.fint.model.resource.utdanning.timeplan.TimeResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("utdanningTimeService")
public class TimeService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public TimeResources getTimeResources(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        return webClientRequest.get(
                ResourceUrlBuilder.urlWithQueryParams(
                    endpoints.getUtdanningTimeplan() + "/time",
                    sinceTimeStamp),
                TimeResources.class,
                dfe);
    }

    public TimeResource getTimeResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, TimeResource.class, dfe);
    }
}

