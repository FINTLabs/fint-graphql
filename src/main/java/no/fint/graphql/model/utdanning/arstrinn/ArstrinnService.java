// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.arstrinn;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.utdanningsprogram.ArstrinnResource;
import no.fint.model.resource.utdanning.utdanningsprogram.ArstrinnResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("utdanningArstrinnService")
public class ArstrinnService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public ArstrinnResources getArstrinnResources(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        return webClientRequest.get(
                ResourceUrlBuilder.urlWithQueryParams(
                    endpoints.getUtdanningUtdanningsprogram() + "/arstrinn",
                    sinceTimeStamp),
                ArstrinnResources.class,
                dfe);
    }

    public ArstrinnResource getArstrinnResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, ArstrinnResource.class, dfe);
    }
}

