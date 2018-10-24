// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.personalressurskategori;

import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.administrasjon.kodeverk.PersonalressurskategoriResource;
import no.fint.model.resource.administrasjon.kodeverk.PersonalressurskategoriResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class PersonalressurskategoriService {

    @Autowired
    private WebClient webClient;

    public PersonalressurskategoriResources getPersonalressurskategoriResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams("administrasjon/kodeverk/personalressurskategori", sinceTimeStamp))
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

