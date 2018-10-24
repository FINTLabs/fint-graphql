// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.elevkategori;

import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.utdanning.kodeverk.ElevkategoriResource;
import no.fint.model.resource.utdanning.kodeverk.ElevkategoriResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ElevkategoriService {

    @Autowired
    private WebClient webClient;

    public ElevkategoriResources getElevkategoriResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams("utdanning/kodeverk/elevkategori", sinceTimeStamp))
                .retrieve()
                .bodyToMono(ElevkategoriResources.class)
                .block();
    }

    public ElevkategoriResource getElevkategoriResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(ElevkategoriResource.class)
                .block();
    }
}

