// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.basisgruppe;

import no.fint.graphql.model.Endpoints;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.utdanning.elev.BasisgruppeResource;
import no.fint.model.resource.utdanning.elev.BasisgruppeResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service("utdanningBasisgruppeService")
public class BasisgruppeService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private Endpoints endpoints;

    public BasisgruppeResources getBasisgruppeResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams(endpoints.getUtdanningElev() + "/basisgruppe", sinceTimeStamp))
                .retrieve()
                .bodyToMono(BasisgruppeResources.class)
                .block();
    }

    public BasisgruppeResource getBasisgruppeResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(BasisgruppeResource.class)
                .block();
    }
}

