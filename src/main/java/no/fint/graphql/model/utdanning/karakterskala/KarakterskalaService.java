// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.karakterskala;

import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.utdanning.kodeverk.KarakterskalaResource;
import no.fint.model.resource.utdanning.kodeverk.KarakterskalaResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class KarakterskalaService {

    @Autowired
    private WebClient webClient;

    public KarakterskalaResources getKarakterskalaResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams("utdanning/kodeverk/karakterskala", sinceTimeStamp))
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

