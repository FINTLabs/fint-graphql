// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.undervisningsforhold;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.elev.UndervisningsforholdResource;
import no.fint.model.resource.utdanning.elev.UndervisningsforholdResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("utdanningUndervisningsforholdService")
public class UndervisningsforholdService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public UndervisningsforholdResources getUndervisningsforholdResources(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        return webClientRequest.get(
                ResourceUrlBuilder.urlWithQueryParams(
                    endpoints.getUtdanningElev() + "/undervisningsforhold",
                    sinceTimeStamp),
                UndervisningsforholdResources.class,
                dfe);
    }

    public UndervisningsforholdResource getUndervisningsforholdResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, UndervisningsforholdResource.class, dfe);
    }
}

