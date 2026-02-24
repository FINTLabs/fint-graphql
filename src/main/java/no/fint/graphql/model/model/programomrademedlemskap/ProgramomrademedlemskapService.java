
package no.fint.graphql.model.model.programomrademedlemskap;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.utdanning.utdanningsprogram.ProgramomrademedlemskapResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelProgramomrademedlemskapService")
public class ProgramomrademedlemskapService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<ProgramomrademedlemskapResource> getProgramomrademedlemskapResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getProgramomrademedlemskapResource(
            endpoints.getUtdanningUtdanningsprogram() 
                + "/programomrademedlemskap/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<ProgramomrademedlemskapResource> getProgramomrademedlemskapResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, ProgramomrademedlemskapResource.class, dfe);
    }
}

