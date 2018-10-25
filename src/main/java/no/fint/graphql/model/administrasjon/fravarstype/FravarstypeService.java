// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.fravarstype;

import no.fint.graphql.model.Endpoints;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.administrasjon.kodeverk.FravarstypeResource;
import no.fint.model.resource.administrasjon.kodeverk.FravarstypeResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service("administrasjonFravarstypeService")
public class FravarstypeService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private Endpoints endpoints;

    public FravarstypeResources getFravarstypeResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams(endpoints.getAdministrasjonKodeverk() + "/fravarstype", sinceTimeStamp))
                .retrieve()
                .bodyToMono(FravarstypeResources.class)
                .block();
    }

    public FravarstypeResource getFravarstypeResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(FravarstypeResource.class)
                .block();
    }
}

