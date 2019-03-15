
package no.fint.graphql.model.administrasjon.stillingskode;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.administrasjon.kodeverk.StillingskodeResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("administrasjonStillingskodeService")
public class StillingskodeService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public StillingskodeResource getStillingskodeResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getStillingskodeResource(
            endpoints.getAdministrasjonKodeverk() 
                + "/stillingskode/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public StillingskodeResource getStillingskodeResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, StillingskodeResource.class, dfe);
    }
}

