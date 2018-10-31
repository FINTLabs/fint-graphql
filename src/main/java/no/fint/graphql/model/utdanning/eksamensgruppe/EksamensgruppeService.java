// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.eksamensgruppe;

import no.fint.graphql.model.Endpoints;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.utdanning.vurdering.EksamensgruppeResource;
import no.fint.model.resource.utdanning.vurdering.EksamensgruppeResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service("utdanningEksamensgruppeService")
public class EksamensgruppeService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private Endpoints endpoints;

    public EksamensgruppeResources getEksamensgruppeResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams(endpoints.getUtdanningVurdering() + "/eksamensgruppe", sinceTimeStamp))
                .retrieve()
                .bodyToMono(EksamensgruppeResources.class)
                .block();
    }

    public EksamensgruppeResource getEksamensgruppeResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(EksamensgruppeResource.class)
                .block();
    }
}

