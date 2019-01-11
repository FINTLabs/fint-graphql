// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.uketimetall;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.administrasjon.kodeverk.UketimetallResource;
import no.fint.model.resource.administrasjon.kodeverk.UketimetallResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("administrasjonUketimetallService")
public class UketimetallService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public UketimetallResources getUketimetallResources(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        return webClientRequest.get(
                ResourceUrlBuilder.urlWithQueryParams(
                    endpoints.getAdministrasjonKodeverk() + "/uketimetall",
                    sinceTimeStamp),
                UketimetallResources.class,
                dfe);
    }

    public UketimetallResource getUketimetallResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, UketimetallResource.class, dfe);
    }
}

