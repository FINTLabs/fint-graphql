// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.utdanningsprogram;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.utdanningsprogram.UtdanningsprogramResource;
import no.fint.model.resource.utdanning.utdanningsprogram.UtdanningsprogramResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("utdanningUtdanningsprogramService")
public class UtdanningsprogramService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public UtdanningsprogramResources getUtdanningsprogramResources(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        return webClientRequest.get(
                ResourceUrlBuilder.urlWithQueryParams(
                    endpoints.getUtdanningUtdanningsprogram() + "/utdanningsprogram",
                    sinceTimeStamp),
                UtdanningsprogramResources.class,
                dfe);
    }

    public UtdanningsprogramResource getUtdanningsprogramResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, UtdanningsprogramResource.class, dfe);
    }
}

