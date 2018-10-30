// Built from tag master

package no.fint.graphql.model.utdanning.utdanningsprogram;

import no.fint.graphql.model.Endpoints;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.utdanning.utdanningsprogram.UtdanningsprogramResource;
import no.fint.model.resource.utdanning.utdanningsprogram.UtdanningsprogramResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service("utdanningUtdanningsprogramService")
public class UtdanningsprogramService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private Endpoints endpoints;

    public UtdanningsprogramResources getUtdanningsprogramResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams(endpoints.getUtdanningUtdanningsprogram() + "/utdanningsprogram", sinceTimeStamp))
                .retrieve()
                .bodyToMono(UtdanningsprogramResources.class)
                .block();
    }

    public UtdanningsprogramResource getUtdanningsprogramResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(UtdanningsprogramResource.class)
                .block();
    }
}

