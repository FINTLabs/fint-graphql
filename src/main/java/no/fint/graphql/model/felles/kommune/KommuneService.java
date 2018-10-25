// Built from tag v3.1.0

package no.fint.graphql.model.felles.kommune;

import no.fint.graphql.model.Endpoints;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.felles.kodeverk.KommuneResource;
import no.fint.model.resource.felles.kodeverk.KommuneResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service("fellesKommuneService")
public class KommuneService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private Endpoints endpoints;

    public KommuneResources getKommuneResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams(endpoints.getFellesKodeverk() + "/kommune", sinceTimeStamp))
                .retrieve()
                .bodyToMono(KommuneResources.class)
                .block();
    }

    public KommuneResource getKommuneResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(KommuneResource.class)
                .block();
    }
}

