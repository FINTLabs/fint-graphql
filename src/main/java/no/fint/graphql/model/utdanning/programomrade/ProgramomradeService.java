// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.programomrade;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.utdanningsprogram.ProgramomradeResource;
import no.fint.model.resource.utdanning.utdanningsprogram.ProgramomradeResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("utdanningProgramomradeService")
public class ProgramomradeService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public ProgramomradeResources getProgramomradeResources(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        return webClientRequest.get(
                ResourceUrlBuilder.urlWithQueryParams(
                    endpoints.getUtdanningUtdanningsprogram() + "/programomrade",
                    sinceTimeStamp),
                ProgramomradeResources.class,
                dfe);
    }

    public ProgramomradeResource getProgramomradeResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, ProgramomradeResource.class, dfe);
    }
}

