// Built from tag v3.1.0

package no.fint.graphql.model.felles.sprak;

import no.fint.graphql.model.Endpoints;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.felles.kodeverk.iso.SprakResource;
import no.fint.model.resource.felles.kodeverk.iso.SprakResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service("fellesSprakService")
public class SprakService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private Endpoints endpoints;

    public SprakResources getSprakResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams(endpoints.getFellesKodeverkIso() + "/sprak", sinceTimeStamp))
                .retrieve()
                .bodyToMono(SprakResources.class)
                .block();
    }

    public SprakResource getSprakResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(SprakResource.class)
                .block();
    }
}

