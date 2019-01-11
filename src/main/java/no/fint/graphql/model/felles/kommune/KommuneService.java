// Built from tag v3.1.0

package no.fint.graphql.model.felles.kommune;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.felles.kodeverk.KommuneResource;
import no.fint.model.resource.felles.kodeverk.KommuneResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("fellesKommuneService")
public class KommuneService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public KommuneResources getKommuneResources(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        return webClientRequest.get(
                ResourceUrlBuilder.urlWithQueryParams(
                    endpoints.getFellesKodeverk() + "/kommune",
                    sinceTimeStamp),
                KommuneResources.class,
                dfe);
    }

    public KommuneResource getKommuneResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, KommuneResource.class, dfe);
    }
}

