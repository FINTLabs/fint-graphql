// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.rom;

import no.fint.graphql.model.Endpoints;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.utdanning.timeplan.RomResource;
import no.fint.model.resource.utdanning.timeplan.RomResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service("utdanningRomService")
public class RomService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private Endpoints endpoints;

    public RomResources getRomResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams(endpoints.getUtdanningTimeplan() + "/rom", sinceTimeStamp))
                .retrieve()
                .bodyToMono(RomResources.class)
                .block();
    }

    public RomResource getRomResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(RomResource.class)
                .block();
    }
}

