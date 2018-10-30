// Built from tag master

package no.fint.graphql.model.administrasjon.art;

import no.fint.graphql.model.Endpoints;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.administrasjon.kodeverk.ArtResource;
import no.fint.model.resource.administrasjon.kodeverk.ArtResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service("administrasjonArtService")
public class ArtService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private Endpoints endpoints;

    public ArtResources getArtResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams(endpoints.getAdministrasjonKodeverk() + "/art", sinceTimeStamp))
                .retrieve()
                .bodyToMono(ArtResources.class)
                .block();
    }

    public ArtResource getArtResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(ArtResource.class)
                .block();
    }
}

