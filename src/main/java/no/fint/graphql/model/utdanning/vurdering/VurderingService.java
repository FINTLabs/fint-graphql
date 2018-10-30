// Built from tag master

package no.fint.graphql.model.utdanning.vurdering;

import no.fint.graphql.model.Endpoints;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.utdanning.vurdering.VurderingResource;
import no.fint.model.resource.utdanning.vurdering.VurderingResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service("utdanningVurderingService")
public class VurderingService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private Endpoints endpoints;

    public VurderingResources getVurderingResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams(endpoints.getUtdanningVurdering() + "/vurdering", sinceTimeStamp))
                .retrieve()
                .bodyToMono(VurderingResources.class)
                .block();
    }

    public VurderingResource getVurderingResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(VurderingResource.class)
                .block();
    }
}

