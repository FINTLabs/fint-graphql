// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.vurdering;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.vurdering.VurderingResource;
import no.fint.model.resource.utdanning.vurdering.VurderingResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("utdanningVurderingService")
public class VurderingService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public VurderingResources getVurderingResources(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        return webClientRequest.get(
                ResourceUrlBuilder.urlWithQueryParams(
                    endpoints.getUtdanningVurdering() + "/vurdering",
                    sinceTimeStamp),
                VurderingResources.class,
                dfe);
    }

    public VurderingResource getVurderingResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, VurderingResource.class, dfe);
    }
}

