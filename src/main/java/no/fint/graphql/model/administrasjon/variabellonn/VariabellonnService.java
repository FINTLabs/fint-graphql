// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.variabellonn;

import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.administrasjon.personal.VariabellonnResource;
import no.fint.model.resource.administrasjon.personal.VariabellonnResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class VariabellonnService {

    @Autowired
    private WebClient webClient;

    public VariabellonnResources getVariabellonnResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams("administrasjon/personal/variabellonn", sinceTimeStamp))
                .retrieve()
                .bodyToMono(VariabellonnResources.class)
                .block();
    }

    public VariabellonnResource getVariabellonnResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(VariabellonnResource.class)
                .block();
    }
}

