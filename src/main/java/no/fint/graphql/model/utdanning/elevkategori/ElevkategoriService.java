// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.elevkategori;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.kodeverk.ElevkategoriResource;
import no.fint.model.resource.utdanning.kodeverk.ElevkategoriResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("utdanningElevkategoriService")
public class ElevkategoriService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public ElevkategoriResources getElevkategoriResources(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        return webClientRequest.get(
                ResourceUrlBuilder.urlWithQueryParams(
                    endpoints.getUtdanningKodeverk() + "/elevkategori",
                    sinceTimeStamp),
                ElevkategoriResources.class,
                dfe);
    }

    public ElevkategoriResource getElevkategoriResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, ElevkategoriResource.class, dfe);
    }
}

