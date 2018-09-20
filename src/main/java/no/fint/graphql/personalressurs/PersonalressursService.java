package no.fint.graphql.personalressurs;

import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.administrasjon.personal.PersonalressursResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class PersonalressursService {

    @Autowired
    private WebClient webClient;

    public PersonalressursResources getPersonalressursResources(String sinceTimeStamp) {
        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams("/administrasjon/personal/personalressurs", sinceTimeStamp))
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
