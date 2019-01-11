// Built from tag v3.1.0

package no.fint.graphql.model.felles.fylke;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.felles.kodeverk.FylkeResource;
import no.fint.model.resource.felles.kodeverk.FylkeResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("fellesFylkeService")
public class FylkeService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public FylkeResources getFylkeResources(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        return webClientRequest.get(
                ResourceUrlBuilder.urlWithQueryParams(
                    endpoints.getFellesKodeverk() + "/fylke",
                    sinceTimeStamp),
                FylkeResources.class,
                dfe);
    }

    public FylkeResource getFylkeResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, FylkeResource.class, dfe);
    }
}

