// Built from tag master

package no.fint.graphql.model.administrasjon.uketimetall;

import no.fint.graphql.model.Endpoints;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.administrasjon.kodeverk.UketimetallResource;
import no.fint.model.resource.administrasjon.kodeverk.UketimetallResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service("administrasjonUketimetallService")
public class UketimetallService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private Endpoints endpoints;

    public UketimetallResources getUketimetallResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams(endpoints.getAdministrasjonKodeverk() + "/uketimetall", sinceTimeStamp))
                .retrieve()
                .bodyToMono(UketimetallResources.class)
                .block();
    }

    public UketimetallResource getUketimetallResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(UketimetallResource.class)
                .block();
    }
}

