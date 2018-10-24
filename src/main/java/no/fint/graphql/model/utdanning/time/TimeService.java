// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.time;

import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.utdanning.timeplan.TimeResource;
import no.fint.model.resource.utdanning.timeplan.TimeResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class TimeService {

    @Autowired
    private WebClient webClient;

    public TimeResources getTimeResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams("utdanning/timeplan/time", sinceTimeStamp))
                .retrieve()
                .bodyToMono(TimeResources.class)
                .block();
    }

    public TimeResource getTimeResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(TimeResource.class)
                .block();
    }
}

