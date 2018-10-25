// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.karakterskala;

import no.fint.graphql.model.Endpoints;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.utdanning.kodeverk.KarakterskalaResource;
import no.fint.model.resource.utdanning.kodeverk.KarakterskalaResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service("utdanningKarakterskalaService")
public class KarakterskalaService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private Endpoints endpoints;

    public KarakterskalaResources getKarakterskalaResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams(endpoints.getUtdanningKodeverk() + "/karakterskala", sinceTimeStamp))
                .retrieve()
                .bodyToMono(KarakterskalaResources.class)
                .block();
    }

    public KarakterskalaResource getKarakterskalaResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(KarakterskalaResource.class)
                .block();
    }
}

