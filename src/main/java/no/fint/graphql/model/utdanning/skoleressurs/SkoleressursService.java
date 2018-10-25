// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.skoleressurs;

import no.fint.graphql.model.Endpoints;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.utdanning.elev.SkoleressursResource;
import no.fint.model.resource.utdanning.elev.SkoleressursResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service("utdanningSkoleressursService")
public class SkoleressursService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private Endpoints endpoints;

    public SkoleressursResources getSkoleressursResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams(endpoints.getUtdanningElev() + "/skoleressurs", sinceTimeStamp))
                .retrieve()
                .bodyToMono(SkoleressursResources.class)
                .block();
    }

    public SkoleressursResource getSkoleressursResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(SkoleressursResource.class)
                .block();
    }
}

