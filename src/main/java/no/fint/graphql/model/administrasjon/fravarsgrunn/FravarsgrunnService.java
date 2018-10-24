// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.fravarsgrunn;

import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.administrasjon.kodeverk.FravarsgrunnResource;
import no.fint.model.resource.administrasjon.kodeverk.FravarsgrunnResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class FravarsgrunnService {

    @Autowired
    private WebClient webClient;

    public FravarsgrunnResources getFravarsgrunnResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams("administrasjon/kodeverk/fravarsgrunn", sinceTimeStamp))
                .retrieve()
                .bodyToMono(FravarsgrunnResources.class)
                .block();
    }

    public FravarsgrunnResource getFravarsgrunnResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(FravarsgrunnResource.class)
                .block();
    }
}

