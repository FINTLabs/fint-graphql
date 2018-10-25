// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.fastlonn;

import no.fint.graphql.model.Endpoints;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.administrasjon.personal.FastlonnResource;
import no.fint.model.resource.administrasjon.personal.FastlonnResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service("administrasjonFastlonnService")
public class FastlonnService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private Endpoints endpoints;

    public FastlonnResources getFastlonnResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams(endpoints.getAdministrasjonPersonal() + "/fastlonn", sinceTimeStamp))
                .retrieve()
                .bodyToMono(FastlonnResources.class)
                .block();
    }

    public FastlonnResource getFastlonnResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(FastlonnResource.class)
                .block();
    }
}

