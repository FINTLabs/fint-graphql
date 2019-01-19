// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.skoleressurs;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.elev.SkoleressursResource;
import no.fint.model.resource.utdanning.elev.SkoleressursResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("utdanningSkoleressursService")
public class SkoleressursService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public SkoleressursResources getSkoleressursResources(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        return webClientRequest.get(
                ResourceUrlBuilder.urlWithQueryParams(
                    endpoints.getUtdanningElev() + "/skoleressurs",
                    sinceTimeStamp),
                SkoleressursResources.class,
                dfe);
    }

    public SkoleressursResource getSkoleressursResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, SkoleressursResource.class, dfe);
    }
}

