package no.fint.graphql.personalressurs;

import no.fint.model.resource.administrasjon.personal.PersonalressursResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class PersonalressursService {

    @Autowired
    private WebClient webClient;

    public PersonalressursResources getPersonalressurs() {
        return webClient.get()
                .uri("/administrasjon/personal/personalressurs")
                .retrieve()
                .bodyToMono(PersonalressursResources.class)
                .block();
    }

}
