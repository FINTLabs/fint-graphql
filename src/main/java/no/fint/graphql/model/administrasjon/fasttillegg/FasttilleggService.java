// Built from tag master

package no.fint.graphql.model.administrasjon.fasttillegg;

import no.fint.graphql.model.Endpoints;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.administrasjon.personal.FasttilleggResource;
import no.fint.model.resource.administrasjon.personal.FasttilleggResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service("administrasjonFasttilleggService")
public class FasttilleggService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private Endpoints endpoints;

    public FasttilleggResources getFasttilleggResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams(endpoints.getAdministrasjonPersonal() + "/fasttillegg", sinceTimeStamp))
                .retrieve()
                .bodyToMono(FasttilleggResources.class)
                .block();
    }

    public FasttilleggResource getFasttilleggResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(FasttilleggResource.class)
                .block();
    }
}

