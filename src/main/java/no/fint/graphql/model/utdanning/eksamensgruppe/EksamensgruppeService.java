// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.eksamensgruppe;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.vurdering.EksamensgruppeResource;
import no.fint.model.resource.utdanning.vurdering.EksamensgruppeResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("utdanningEksamensgruppeService")
public class EksamensgruppeService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public EksamensgruppeResources getEksamensgruppeResources(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        return webClientRequest.get(
                ResourceUrlBuilder.urlWithQueryParams(
                    endpoints.getUtdanningVurdering() + "/eksamensgruppe",
                    sinceTimeStamp),
                EksamensgruppeResources.class,
                dfe);
    }

    public EksamensgruppeResource getEksamensgruppeResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, EksamensgruppeResource.class, dfe);
    }
}

