// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.lonnsart;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.administrasjon.kodeverk.LonnsartResource;
import no.fint.model.resource.administrasjon.kodeverk.LonnsartResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("administrasjonLonnsartService")
public class LonnsartService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public LonnsartResources getLonnsartResources(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        return webClientRequest.get(
                ResourceUrlBuilder.urlWithQueryParams(
                    endpoints.getAdministrasjonKodeverk() + "/lonnsart",
                    sinceTimeStamp),
                LonnsartResources.class,
                dfe);
    }

    public LonnsartResource getLonnsartResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, LonnsartResource.class, dfe);
    }
}

