// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.skole;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("utdanningSkoleService")
public class SkoleService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public SkoleResources getSkoleResources(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        return webClientRequest.get(
                ResourceUrlBuilder.urlWithQueryParams(
                    endpoints.getUtdanningUtdanningsprogram() + "/skole",
                    sinceTimeStamp),
                SkoleResources.class,
                dfe);
    }

    public SkoleResource getSkoleResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, SkoleResource.class, dfe);
    }
}

