// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.karakterverdi;

import no.fint.graphql.model.Endpoints;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.utdanning.vurdering.KarakterverdiResource;
import no.fint.model.resource.utdanning.vurdering.KarakterverdiResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service("utdanningKarakterverdiService")
public class KarakterverdiService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private Endpoints endpoints;

    public KarakterverdiResources getKarakterverdiResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams(endpoints.getUtdanningVurdering() + "/karakterverdi", sinceTimeStamp))
                .retrieve()
                .bodyToMono(KarakterverdiResources.class)
                .block();
    }

    public KarakterverdiResource getKarakterverdiResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(KarakterverdiResource.class)
                .block();
    }
}

