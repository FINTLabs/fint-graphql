// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.undervisningsforhold;

import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.utdanning.elev.UndervisningsforholdResource;
import no.fint.model.resource.utdanning.elev.UndervisningsforholdResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class UndervisningsforholdService {

    @Autowired
    private WebClient webClient;

    public UndervisningsforholdResources getUndervisningsforholdResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams("utdanning/elev/undervisningsforhold", sinceTimeStamp))
                .retrieve()
                .bodyToMono(UndervisningsforholdResources.class)
                .block();
    }

    public UndervisningsforholdResource getUndervisningsforholdResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(UndervisningsforholdResource.class)
                .block();
    }
}

