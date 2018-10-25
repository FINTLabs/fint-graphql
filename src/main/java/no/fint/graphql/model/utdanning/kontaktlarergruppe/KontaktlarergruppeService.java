// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.kontaktlarergruppe;

import no.fint.graphql.model.Endpoints;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.utdanning.elev.KontaktlarergruppeResource;
import no.fint.model.resource.utdanning.elev.KontaktlarergruppeResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service("utdanningKontaktlarergruppeService")
public class KontaktlarergruppeService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private Endpoints endpoints;

    public KontaktlarergruppeResources getKontaktlarergruppeResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams(endpoints.getUtdanningElev() + "/kontaktlarergruppe", sinceTimeStamp))
                .retrieve()
                .bodyToMono(KontaktlarergruppeResources.class)
                .block();
    }

    public KontaktlarergruppeResource getKontaktlarergruppeResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(KontaktlarergruppeResource.class)
                .block();
    }
}

