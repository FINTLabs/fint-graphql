// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.personalressurskategori;

import no.fint.graphql.model.Endpoints;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.administrasjon.kodeverk.PersonalressurskategoriResource;
import no.fint.model.resource.administrasjon.kodeverk.PersonalressurskategoriResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service("administrasjonPersonalressurskategoriService")
public class PersonalressurskategoriService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private Endpoints endpoints;

    public PersonalressurskategoriResources getPersonalressurskategoriResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams(endpoints.getAdministrasjonKodeverk() + "/personalressurskategori", sinceTimeStamp))
                .retrieve()
                .bodyToMono(PersonalressurskategoriResources.class)
                .block();
    }

    public PersonalressurskategoriResource getPersonalressurskategoriResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(PersonalressurskategoriResource.class)
                .block();
    }
}

