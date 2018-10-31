// Built from tag v3.1.0

package no.fint.graphql.model.felles.kontaktperson;

import no.fint.graphql.model.Endpoints;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.felles.KontaktpersonResource;
import no.fint.model.resource.felles.KontaktpersonResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service("fellesKontaktpersonService")
public class KontaktpersonService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private Endpoints endpoints;

    public KontaktpersonResources getKontaktpersonResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams(endpoints.getFelles() + "/kontaktperson", sinceTimeStamp))
                .retrieve()
                .bodyToMono(KontaktpersonResources.class)
                .block();
    }

    public KontaktpersonResource getKontaktpersonResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(KontaktpersonResource.class)
                .block();
    }
}

