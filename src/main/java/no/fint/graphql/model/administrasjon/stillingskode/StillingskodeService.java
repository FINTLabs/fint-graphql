// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.stillingskode;

import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.administrasjon.kodeverk.StillingskodeResource;
import no.fint.model.resource.administrasjon.kodeverk.StillingskodeResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class StillingskodeService {

    @Autowired
    private WebClient webClient;

    public StillingskodeResources getStillingskodeResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams("administrasjon/kodeverk/stillingskode", sinceTimeStamp))
                .retrieve()
                .bodyToMono(StillingskodeResources.class)
                .block();
    }

    public StillingskodeResource getStillingskodeResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(StillingskodeResource.class)
                .block();
    }
}

