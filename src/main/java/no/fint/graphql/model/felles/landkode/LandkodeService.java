// Built from tag v3.1.0

package no.fint.graphql.model.felles.landkode;

import no.fint.graphql.model.Endpoints;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.felles.kodeverk.iso.LandkodeResource;
import no.fint.model.resource.felles.kodeverk.iso.LandkodeResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service("fellesLandkodeService")
public class LandkodeService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private Endpoints endpoints;

    public LandkodeResources getLandkodeResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams(endpoints.getFellesKodeverkIso() + "/landkode", sinceTimeStamp))
                .retrieve()
                .bodyToMono(LandkodeResources.class)
                .block();
    }

    public LandkodeResource getLandkodeResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(LandkodeResource.class)
                .block();
    }
}

