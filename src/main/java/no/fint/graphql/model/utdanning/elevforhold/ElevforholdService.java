// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.elevforhold;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.fint.model.resource.utdanning.elev.ElevforholdResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("utdanningElevforholdService")
public class ElevforholdService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public ElevforholdResources getElevforholdResources(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        return webClientRequest.get(
                ResourceUrlBuilder.urlWithQueryParams(
                    endpoints.getUtdanningElev() + "/elevforhold",
                    sinceTimeStamp),
                ElevforholdResources.class,
                dfe);
    }

    public ElevforholdResource getElevforholdResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, ElevforholdResource.class, dfe);
    }
}

