// Built from tag master

package no.fint.graphql.model.administrasjon.fullmakt;

import no.fint.graphql.model.Endpoints;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.administrasjon.fullmakt.FullmaktResource;
import no.fint.model.resource.administrasjon.fullmakt.FullmaktResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service("administrasjonFullmaktService")
public class FullmaktService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private Endpoints endpoints;

    public FullmaktResources getFullmaktResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams(endpoints.getAdministrasjonFullmakt() + "/fullmakt", sinceTimeStamp))
                .retrieve()
                .bodyToMono(FullmaktResources.class)
                .block();
    }

    public FullmaktResource getFullmaktResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(FullmaktResource.class)
                .block();
    }
}

