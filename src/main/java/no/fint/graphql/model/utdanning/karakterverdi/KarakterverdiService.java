// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.karakterverdi;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.vurdering.KarakterverdiResource;
import no.fint.model.resource.utdanning.vurdering.KarakterverdiResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("utdanningKarakterverdiService")
public class KarakterverdiService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public KarakterverdiResources getKarakterverdiResources(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        return webClientRequest.get(
                ResourceUrlBuilder.urlWithQueryParams(
                    endpoints.getUtdanningVurdering() + "/karakterverdi",
                    sinceTimeStamp),
                KarakterverdiResources.class,
                dfe);
    }

    public KarakterverdiResource getKarakterverdiResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, KarakterverdiResource.class, dfe);
    }
}

