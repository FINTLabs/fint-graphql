// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.fullmakt;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.administrasjon.fullmakt.FullmaktResource;
import no.fint.model.resource.administrasjon.fullmakt.FullmaktResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("administrasjonFullmaktService")
public class FullmaktService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public FullmaktResources getFullmaktResources(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        return webClientRequest.get(
                ResourceUrlBuilder.urlWithQueryParams(
                    endpoints.getAdministrasjonFullmakt() + "/fullmakt",
                    sinceTimeStamp),
                FullmaktResources.class,
                dfe);
    }

    public FullmaktResource getFullmaktResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, FullmaktResource.class, dfe);
    }
}

