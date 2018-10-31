// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.undervisningsgruppe;

import no.fint.graphql.model.Endpoints;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource;
import no.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service("utdanningUndervisningsgruppeService")
public class UndervisningsgruppeService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private Endpoints endpoints;

    public UndervisningsgruppeResources getUndervisningsgruppeResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams(endpoints.getUtdanningTimeplan() + "/undervisningsgruppe", sinceTimeStamp))
                .retrieve()
                .bodyToMono(UndervisningsgruppeResources.class)
                .block();
    }

    public UndervisningsgruppeResource getUndervisningsgruppeResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(UndervisningsgruppeResource.class)
                .block();
    }
}

