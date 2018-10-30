// Built from tag master

package no.fint.graphql.model.utdanning.skoleeiertype;

import no.fint.graphql.model.Endpoints;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.utdanning.kodeverk.SkoleeiertypeResource;
import no.fint.model.resource.utdanning.kodeverk.SkoleeiertypeResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service("utdanningSkoleeiertypeService")
public class SkoleeiertypeService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private Endpoints endpoints;

    public SkoleeiertypeResources getSkoleeiertypeResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams(endpoints.getUtdanningKodeverk() + "/skoleeiertype", sinceTimeStamp))
                .retrieve()
                .bodyToMono(SkoleeiertypeResources.class)
                .block();
    }

    public SkoleeiertypeResource getSkoleeiertypeResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(SkoleeiertypeResource.class)
                .block();
    }
}

