package no.fint.graphql.arbeidsforhold;

import no.fint.model.resource.administrasjon.personal.ArbeidsforholdResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ArbeidsforholdService {

    @Autowired
    private WebClient webClient;

    public ArbeidsforholdResources getArbeidsforholdResources() {
        return webClient.get()
                .uri("/administrasjon/personal/arbeidsforhold")
                .retrieve()
                .bodyToMono(ArbeidsforholdResources.class)
                .block();
    }
}
