// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.medlemskap;

import no.fint.graphql.model.Endpoints;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.utdanning.elev.MedlemskapResource;
import no.fint.model.resource.utdanning.elev.MedlemskapResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service("utdanningMedlemskapService")
public class MedlemskapService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private Endpoints endpoints;

    public MedlemskapResources getMedlemskapResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams(endpoints.getUtdanningElev() + "/medlemskap", sinceTimeStamp))
                .retrieve()
                .bodyToMono(MedlemskapResources.class)
                .block();
    }

    public MedlemskapResource getMedlemskapResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(MedlemskapResource.class)
                .block();
    }
}

