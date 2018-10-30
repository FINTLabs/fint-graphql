// Built from tag master

package no.fint.graphql.model.utdanning.fravar;

import no.fint.graphql.model.Endpoints;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.utdanning.vurdering.FravarResource;
import no.fint.model.resource.utdanning.vurdering.FravarResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service("utdanningFravarService")
public class FravarService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private Endpoints endpoints;

    public FravarResources getFravarResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams(endpoints.getUtdanningVurdering() + "/fravar", sinceTimeStamp))
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

