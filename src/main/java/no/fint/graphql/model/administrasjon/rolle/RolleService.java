// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.rolle;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.administrasjon.fullmakt.RolleResource;
import no.fint.model.resource.administrasjon.fullmakt.RolleResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("administrasjonRolleService")
public class RolleService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public RolleResources getRolleResources(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        return webClientRequest.get(
                ResourceUrlBuilder.urlWithQueryParams(
                    endpoints.getAdministrasjonFullmakt() + "/rolle",
                    sinceTimeStamp),
                RolleResources.class,
                dfe);
    }

    public RolleResource getRolleResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, RolleResource.class, dfe);
    }
}

