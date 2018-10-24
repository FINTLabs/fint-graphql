// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.elev;

import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.utdanning.elev.ElevResource;
import no.fint.model.resource.utdanning.elev.ElevResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ElevService {

    @Autowired
    private WebClient webClient;

    public ElevResources getElevResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams("utdanning/elev/elev", sinceTimeStamp))
                .retrieve()
                .bodyToMono(ElevResources.class)
                .block();
    }

    public ElevResource getElevResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(ElevResource.class)
                .block();
    }
}

