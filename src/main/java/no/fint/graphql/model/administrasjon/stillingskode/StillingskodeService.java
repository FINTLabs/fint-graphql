// Built from tag master

package no.fint.graphql.model.administrasjon.stillingskode;

import no.fint.graphql.model.Endpoints;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.administrasjon.kodeverk.StillingskodeResource;
import no.fint.model.resource.administrasjon.kodeverk.StillingskodeResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service("administrasjonStillingskodeService")
public class StillingskodeService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private Endpoints endpoints;

    public StillingskodeResources getStillingskodeResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams(endpoints.getAdministrasjonKodeverk() + "/stillingskode", sinceTimeStamp))
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

