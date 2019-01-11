// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.fastlonn;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.administrasjon.personal.FastlonnResource;
import no.fint.model.resource.administrasjon.personal.FastlonnResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("administrasjonFastlonnService")
public class FastlonnService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public FastlonnResources getFastlonnResources(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        return webClientRequest.get(
                ResourceUrlBuilder.urlWithQueryParams(
                    endpoints.getAdministrasjonPersonal() + "/fastlonn",
                    sinceTimeStamp),
                FastlonnResources.class,
                dfe);
    }

    public FastlonnResource getFastlonnResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, FastlonnResource.class, dfe);
    }
}

