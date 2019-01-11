// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.variabellonn;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.administrasjon.personal.VariabellonnResource;
import no.fint.model.resource.administrasjon.personal.VariabellonnResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("administrasjonVariabellonnService")
public class VariabellonnService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public VariabellonnResources getVariabellonnResources(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        return webClientRequest.get(
                ResourceUrlBuilder.urlWithQueryParams(
                    endpoints.getAdministrasjonPersonal() + "/variabellonn",
                    sinceTimeStamp),
                VariabellonnResources.class,
                dfe);
    }

    public VariabellonnResource getVariabellonnResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, VariabellonnResource.class, dfe);
    }
}

