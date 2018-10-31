// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.arstrinn;

import no.fint.graphql.model.Endpoints;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.utdanning.utdanningsprogram.ArstrinnResource;
import no.fint.model.resource.utdanning.utdanningsprogram.ArstrinnResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service("utdanningArstrinnService")
public class ArstrinnService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private Endpoints endpoints;

    public ArstrinnResources getArstrinnResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams(endpoints.getUtdanningUtdanningsprogram() + "/arstrinn", sinceTimeStamp))
                .retrieve()
                .bodyToMono(ArstrinnResources.class)
                .block();
    }

    public ArstrinnResource getArstrinnResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(ArstrinnResource.class)
                .block();
    }
}

