// Built from tag master

package no.fint.graphql.model.felles.fylke;

import no.fint.graphql.model.Endpoints;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.felles.kodeverk.FylkeResource;
import no.fint.model.resource.felles.kodeverk.FylkeResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service("fellesFylkeService")
public class FylkeService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private Endpoints endpoints;

    public FylkeResources getFylkeResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams(endpoints.getFellesKodeverk() + "/fylke", sinceTimeStamp))
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

