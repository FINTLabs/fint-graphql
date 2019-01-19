// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.stillingskode;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.administrasjon.kodeverk.StillingskodeResource;
import no.fint.model.resource.administrasjon.kodeverk.StillingskodeResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("administrasjonStillingskodeService")
public class StillingskodeService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public StillingskodeResources getStillingskodeResources(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        return webClientRequest.get(
                ResourceUrlBuilder.urlWithQueryParams(
                    endpoints.getAdministrasjonKodeverk() + "/stillingskode",
                    sinceTimeStamp),
                StillingskodeResources.class,
                dfe);
    }

    public StillingskodeResource getStillingskodeResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, StillingskodeResource.class, dfe);
    }
}

