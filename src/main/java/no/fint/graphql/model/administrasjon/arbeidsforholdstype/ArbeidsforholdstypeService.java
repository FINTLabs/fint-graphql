// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.arbeidsforholdstype;

import no.fint.graphql.model.Endpoints;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.administrasjon.kodeverk.ArbeidsforholdstypeResource;
import no.fint.model.resource.administrasjon.kodeverk.ArbeidsforholdstypeResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service("administrasjonArbeidsforholdstypeService")
public class ArbeidsforholdstypeService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private Endpoints endpoints;

    public ArbeidsforholdstypeResources getArbeidsforholdstypeResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams(endpoints.getAdministrasjonKodeverk() + "/arbeidsforholdstype", sinceTimeStamp))
                .retrieve()
                .bodyToMono(ArbeidsforholdstypeResources.class)
                .block();
    }

    public ArbeidsforholdstypeResource getArbeidsforholdstypeResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(ArbeidsforholdstypeResource.class)
                .block();
    }
}

