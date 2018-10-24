// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.vurdering;

import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.utdanning.vurdering.VurderingResource;
import no.fint.model.resource.utdanning.vurdering.VurderingResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class VurderingService {

    @Autowired
    private WebClient webClient;

    public VurderingResources getVurderingResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams("utdanning/vurdering/vurdering", sinceTimeStamp))
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

