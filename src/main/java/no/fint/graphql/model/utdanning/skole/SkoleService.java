// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.skole;

import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class SkoleService {

    @Autowired
    private WebClient webClient;

    public SkoleResources getSkoleResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams("utdanning/utdanningsprogram/skole", sinceTimeStamp))
                .retrieve()
                .bodyToMono(SkoleResources.class)
                .block();
    }

    public SkoleResource getSkoleResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(SkoleResource.class)
                .block();
    }
}

