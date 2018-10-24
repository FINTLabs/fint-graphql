// Built from tag v3.1.0

package no.fint.graphql.model.felles.kjonn;

import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.felles.kodeverk.iso.KjonnResource;
import no.fint.model.resource.felles.kodeverk.iso.KjonnResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class KjonnService {

    @Autowired
    private WebClient webClient;

    public KjonnResources getKjonnResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams("felles/kodeverk/iso/kjonn", sinceTimeStamp))
                .retrieve()
                .bodyToMono(KjonnResources.class)
                .block();
    }

    public KjonnResource getKjonnResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(KjonnResource.class)
                .block();
    }
}

