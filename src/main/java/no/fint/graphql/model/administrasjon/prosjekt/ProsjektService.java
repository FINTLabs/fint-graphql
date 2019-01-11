// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.prosjekt;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.administrasjon.kodeverk.ProsjektResource;
import no.fint.model.resource.administrasjon.kodeverk.ProsjektResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("administrasjonProsjektService")
public class ProsjektService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public ProsjektResources getProsjektResources(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        return webClientRequest.get(
                ResourceUrlBuilder.urlWithQueryParams(
                    endpoints.getAdministrasjonKodeverk() + "/prosjekt",
                    sinceTimeStamp),
                ProsjektResources.class,
                dfe);
    }

    public ProsjektResource getProsjektResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, ProsjektResource.class, dfe);
    }
}

