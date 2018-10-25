// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.arbeidsforhold;

import no.fint.graphql.model.Endpoints;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.administrasjon.personal.ArbeidsforholdResource;
import no.fint.model.resource.administrasjon.personal.ArbeidsforholdResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service("administrasjonArbeidsforholdService")
public class ArbeidsforholdService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private Endpoints endpoints;

    public ArbeidsforholdResources getArbeidsforholdResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams(endpoints.getAdministrasjonPersonal() + "/arbeidsforhold", sinceTimeStamp))
                .retrieve()
                .bodyToMono(ArbeidsforholdResources.class)
                .block();
    }

    public ArbeidsforholdResource getArbeidsforholdResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(ArbeidsforholdResource.class)
                .block();
    }
}

