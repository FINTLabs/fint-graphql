// Built from tag master

package no.fint.graphql.model.utdanning.elevkategori;

import no.fint.graphql.model.Endpoints;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.utdanning.kodeverk.ElevkategoriResource;
import no.fint.model.resource.utdanning.kodeverk.ElevkategoriResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service("utdanningElevkategoriService")
public class ElevkategoriService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private Endpoints endpoints;

    public ElevkategoriResources getElevkategoriResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams(endpoints.getUtdanningKodeverk() + "/elevkategori", sinceTimeStamp))
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

