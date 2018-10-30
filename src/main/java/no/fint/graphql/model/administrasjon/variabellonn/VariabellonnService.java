// Built from tag master

package no.fint.graphql.model.administrasjon.variabellonn;

import no.fint.graphql.model.Endpoints;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.administrasjon.personal.VariabellonnResource;
import no.fint.model.resource.administrasjon.personal.VariabellonnResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service("administrasjonVariabellonnService")
public class VariabellonnService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private Endpoints endpoints;

    public VariabellonnResources getVariabellonnResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams(endpoints.getAdministrasjonPersonal() + "/variabellonn", sinceTimeStamp))
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

