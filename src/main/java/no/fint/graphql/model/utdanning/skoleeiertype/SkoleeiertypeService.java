// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.skoleeiertype;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.kodeverk.SkoleeiertypeResource;
import no.fint.model.resource.utdanning.kodeverk.SkoleeiertypeResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("utdanningSkoleeiertypeService")
public class SkoleeiertypeService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public SkoleeiertypeResources getSkoleeiertypeResources(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        return webClientRequest.get(
                ResourceUrlBuilder.urlWithQueryParams(
                    endpoints.getUtdanningKodeverk() + "/skoleeiertype",
                    sinceTimeStamp),
                SkoleeiertypeResources.class,
                dfe);
    }

    public SkoleeiertypeResource getSkoleeiertypeResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, SkoleeiertypeResource.class, dfe);
    }
}

