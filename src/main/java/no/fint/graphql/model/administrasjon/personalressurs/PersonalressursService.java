// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.personalressurs;

import no.fint.graphql.model.Endpoints;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.administrasjon.personal.PersonalressursResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service("administrasjonPersonalressursService")
public class PersonalressursService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private Endpoints endpoints;

    public PersonalressursResources getPersonalressursResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams(endpoints.getAdministrasjonPersonal() + "/personalressurs", sinceTimeStamp))
                .retrieve()
                .bodyToMono(PersonalressursResources.class)
                .block();
    }

    public PersonalressursResource getPersonalressursResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(PersonalressursResource.class)
                .block();
    }
}

