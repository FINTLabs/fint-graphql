// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.medlemskap;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.elev.MedlemskapResource;
import no.fint.model.resource.utdanning.elev.MedlemskapResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("utdanningMedlemskapService")
public class MedlemskapService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public MedlemskapResources getMedlemskapResources(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        return webClientRequest.get(
                ResourceUrlBuilder.urlWithQueryParams(
                    endpoints.getUtdanningElev() + "/medlemskap",
                    sinceTimeStamp),
                MedlemskapResources.class,
                dfe);
    }

    public MedlemskapResource getMedlemskapResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, MedlemskapResource.class, dfe);
    }
}

