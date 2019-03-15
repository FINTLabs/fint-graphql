// Built from tag release-3.2

package no.fint.graphql.model.utdanning.programomrade;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.utdanningsprogram.ProgramomradeResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("utdanningProgramomradeService")
public class ProgramomradeService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public ProgramomradeResource getProgramomradeResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getProgramomradeResource(
            endpoints.getUtdanningUtdanningsprogram() 
                + "/programomrade/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public ProgramomradeResource getProgramomradeResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, ProgramomradeResource.class, dfe);
    }
}

