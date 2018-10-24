// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.rolle;

import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.administrasjon.fullmakt.RolleResource;
import no.fint.model.resource.administrasjon.fullmakt.RolleResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class RolleService {

    @Autowired
    private WebClient webClient;

    public RolleResources getRolleResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams("administrasjon/fullmakt/rolle", sinceTimeStamp))
                .retrieve()
                .bodyToMono(RolleResources.class)
                .block();
    }

    public RolleResource getRolleResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(RolleResource.class)
                .block();
    }
}

