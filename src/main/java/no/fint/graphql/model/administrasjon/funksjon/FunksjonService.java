// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.funksjon;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.administrasjon.kodeverk.FunksjonResource;
import no.fint.model.resource.administrasjon.kodeverk.FunksjonResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("administrasjonFunksjonService")
public class FunksjonService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public FunksjonResources getFunksjonResources(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        return webClientRequest.get(
                ResourceUrlBuilder.urlWithQueryParams(
                    endpoints.getAdministrasjonKodeverk() + "/funksjon",
                    sinceTimeStamp),
                FunksjonResources.class,
                dfe);
    }

    public FunksjonResource getFunksjonResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, FunksjonResource.class, dfe);
    }
}

