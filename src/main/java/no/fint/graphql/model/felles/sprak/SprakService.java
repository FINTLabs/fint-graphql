// Built from tag v3.1.0

package no.fint.graphql.model.felles.sprak;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.felles.kodeverk.iso.SprakResource;
import no.fint.model.resource.felles.kodeverk.iso.SprakResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("fellesSprakService")
public class SprakService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public SprakResources getSprakResources(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        return webClientRequest.get(
                ResourceUrlBuilder.urlWithQueryParams(
                    endpoints.getFellesKodeverkIso() + "/sprak",
                    sinceTimeStamp),
                SprakResources.class,
                dfe);
    }

    public SprakResource getSprakResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, SprakResource.class, dfe);
    }
}

