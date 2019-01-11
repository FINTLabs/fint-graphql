// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.fag;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.timeplan.FagResource;
import no.fint.model.resource.utdanning.timeplan.FagResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("utdanningFagService")
public class FagService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public FagResources getFagResources(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        return webClientRequest.get(
                ResourceUrlBuilder.urlWithQueryParams(
                    endpoints.getUtdanningTimeplan() + "/fag",
                    sinceTimeStamp),
                FagResources.class,
                dfe);
    }

    public FagResource getFagResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, FagResource.class, dfe);
    }
}

