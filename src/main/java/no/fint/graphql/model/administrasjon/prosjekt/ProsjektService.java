// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.prosjekt;

import no.fint.graphql.model.Endpoints;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.administrasjon.kodeverk.ProsjektResource;
import no.fint.model.resource.administrasjon.kodeverk.ProsjektResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service("administrasjonProsjektService")
public class ProsjektService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private Endpoints endpoints;

    public ProsjektResources getProsjektResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams(endpoints.getAdministrasjonKodeverk() + "/prosjekt", sinceTimeStamp))
                .retrieve()
                .bodyToMono(ProsjektResources.class)
                .block();
    }

    public ProsjektResource getProsjektResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(ProsjektResource.class)
                .block();
    }
}

