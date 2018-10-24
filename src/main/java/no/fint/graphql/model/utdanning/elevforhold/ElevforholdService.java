// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.elevforhold;

import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.fint.model.resource.utdanning.elev.ElevforholdResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ElevforholdService {

    @Autowired
    private WebClient webClient;

    public ElevforholdResources getElevforholdResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams("utdanning/elev/elevforhold", sinceTimeStamp))
                .retrieve()
                .bodyToMono(ElevforholdResources.class)
                .block();
    }

    public ElevforholdResource getElevforholdResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(ElevforholdResource.class)
                .block();
    }
}

