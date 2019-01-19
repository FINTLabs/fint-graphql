// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.kontaktlarergruppe;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.elev.KontaktlarergruppeResource;
import no.fint.model.resource.utdanning.elev.KontaktlarergruppeResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("utdanningKontaktlarergruppeService")
public class KontaktlarergruppeService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public KontaktlarergruppeResources getKontaktlarergruppeResources(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        return webClientRequest.get(
                ResourceUrlBuilder.urlWithQueryParams(
                    endpoints.getUtdanningElev() + "/kontaktlarergruppe",
                    sinceTimeStamp),
                KontaktlarergruppeResources.class,
                dfe);
    }

    public KontaktlarergruppeResource getKontaktlarergruppeResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, KontaktlarergruppeResource.class, dfe);
    }
}

