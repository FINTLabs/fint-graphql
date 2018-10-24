// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.fravarstype;

import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.utdanning.kodeverk.FravarstypeResource;
import no.fint.model.resource.utdanning.kodeverk.FravarstypeResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class FravarstypeService {

    @Autowired
    private WebClient webClient;

    public FravarstypeResources getFravarstypeResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams("utdanning/kodeverk/fravarstype", sinceTimeStamp))
                .retrieve()
                .bodyToMono(FravarstypeResources.class)
                .block();
    }

    public FravarstypeResource getFravarstypeResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(FravarstypeResource.class)
                .block();
    }
}

