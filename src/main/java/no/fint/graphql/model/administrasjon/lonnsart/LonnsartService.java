// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.lonnsart;

import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.administrasjon.kodeverk.LonnsartResource;
import no.fint.model.resource.administrasjon.kodeverk.LonnsartResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class LonnsartService {

    @Autowired
    private WebClient webClient;

    public LonnsartResources getLonnsartResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams("administrasjon/kodeverk/lonnsart", sinceTimeStamp))
                .retrieve()
                .bodyToMono(LonnsartResources.class)
                .block();
    }

    public LonnsartResource getLonnsartResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(LonnsartResource.class)
                .block();
    }
}

