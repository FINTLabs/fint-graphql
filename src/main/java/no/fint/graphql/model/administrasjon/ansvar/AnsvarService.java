// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.ansvar;

import no.fint.graphql.model.Endpoints;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.administrasjon.kodeverk.AnsvarResource;
import no.fint.model.resource.administrasjon.kodeverk.AnsvarResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service("administrasjonAnsvarService")
public class AnsvarService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private Endpoints endpoints;

    public AnsvarResources getAnsvarResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams(endpoints.getAdministrasjonKodeverk() + "/ansvar", sinceTimeStamp))
                .retrieve()
                .bodyToMono(AnsvarResources.class)
                .block();
    }

    public AnsvarResource getAnsvarResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(AnsvarResource.class)
                .block();
    }
}

