// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.fag;

import no.fint.graphql.model.Endpoints;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.utdanning.timeplan.FagResource;
import no.fint.model.resource.utdanning.timeplan.FagResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service("utdanningFagService")
public class FagService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private Endpoints endpoints;

    public FagResources getFagResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams(endpoints.getUtdanningTimeplan() + "/fag", sinceTimeStamp))
                .retrieve()
                .bodyToMono(FagResources.class)
                .block();
    }

    public FagResource getFagResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(FagResource.class)
                .block();
    }
}

