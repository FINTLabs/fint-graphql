// Built from tag master

package no.fint.graphql.model.utdanning.programomrade;

import no.fint.graphql.model.Endpoints;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.model.resource.utdanning.utdanningsprogram.ProgramomradeResource;
import no.fint.model.resource.utdanning.utdanningsprogram.ProgramomradeResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service("utdanningProgramomradeService")
public class ProgramomradeService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private Endpoints endpoints;

    public ProgramomradeResources getProgramomradeResources(String sinceTimeStamp) {


        return webClient.get()
                .uri(ResourceUrlBuilder.urlWithQueryParams(endpoints.getUtdanningUtdanningsprogram() + "/programomrade", sinceTimeStamp))
                .retrieve()
                .bodyToMono(ProgramomradeResources.class)
                .block();
    }

    public ProgramomradeResource getProgramomradeResource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(ProgramomradeResource.class)
                .block();
    }
}

