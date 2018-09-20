package no.fint.graphql.person;

import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.felles.PersonResource;
import no.fint.model.resource.felles.PersonResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class PersonService {

    @Autowired
    private WebClient webClient;

    public PersonResources getPersonResources(String sinceTimeStamp) {
        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams("/administrasjon/personal/person", sinceTimeStamp))
                .retrieve()
                .bodyToMono(PersonResources.class)
                .block();
    }

    public PersonResource getPersonResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(PersonResource.class)
                .block();
    }
}
