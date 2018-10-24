// Built from tag v3.1.0

package no.fint.graphql.model.felles.fylke;

import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.felles.kodeverk.FylkeResource;
import no.fint.model.resource.felles.kodeverk.FylkeResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class FylkeService {

    @Autowired
    private WebClient webClient;

    public FylkeResources getFylkeResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams("felles/kodeverk/fylke", sinceTimeStamp))
                .retrieve()
                .bodyToMono(FylkeResources.class)
                .block();
    }

    public FylkeResource getFylkeResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(FylkeResource.class)
                .block();
    }
}

