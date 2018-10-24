// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.organisasjonselement;

import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource;
import no.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class OrganisasjonselementService {

    @Autowired
    private WebClient webClient;

    public OrganisasjonselementResources getOrganisasjonselementResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams("administrasjon/organisasjon/organisasjonselement", sinceTimeStamp))
                .retrieve()
                .bodyToMono(OrganisasjonselementResources.class)
                .block();
    }

    public OrganisasjonselementResource getOrganisasjonselementResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(OrganisasjonselementResource.class)
                .block();
    }
}

