// Built from tag master

package no.fint.graphql.model.administrasjon.funksjon;

import no.fint.graphql.model.Endpoints;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.administrasjon.kodeverk.FunksjonResource;
import no.fint.model.resource.administrasjon.kodeverk.FunksjonResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service("administrasjonFunksjonService")
public class FunksjonService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private Endpoints endpoints;

    public FunksjonResources getFunksjonResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams(endpoints.getAdministrasjonKodeverk() + "/funksjon", sinceTimeStamp))
                .retrieve()
                .bodyToMono(FunksjonResources.class)
                .block();
    }

    public FunksjonResource getFunksjonResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(FunksjonResource.class)
                .block();
    }
}

