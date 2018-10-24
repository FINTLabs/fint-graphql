// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.fravar;

import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.utdanning.vurdering.FravarResource;
import no.fint.model.resource.utdanning.vurdering.FravarResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class FravarService {

    @Autowired
    private WebClient webClient;

    public FravarResources getFravarResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams("utdanning/vurdering/fravar", sinceTimeStamp))
                .retrieve()
                .bodyToMono(FravarResources.class)
                .block();
    }

    public FravarResource getFravarResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(FravarResource.class)
                .block();
    }
}

