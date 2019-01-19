// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.fasttillegg;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.administrasjon.personal.FasttilleggResource;
import no.fint.model.resource.administrasjon.personal.FasttilleggResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("administrasjonFasttilleggService")
public class FasttilleggService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public FasttilleggResources getFasttilleggResources(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        return webClientRequest.get(
                ResourceUrlBuilder.urlWithQueryParams(
                    endpoints.getAdministrasjonPersonal() + "/fasttillegg",
                    sinceTimeStamp),
                FasttilleggResources.class,
                dfe);
    }

    public FasttilleggResource getFasttilleggResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, FasttilleggResource.class, dfe);
    }
}

