
package no.fint.graphql.model.model.programomrademedlemskap;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.utdanningsprogram.ProgramomrademedlemskapResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelProgramomrademedlemskapQueryResolver")
@Slf4j
public class ProgramomrademedlemskapQueryResolver {

    @Autowired
    private ProgramomrademedlemskapService service;

    @QueryMapping(name = "programomrademedlemskap")
    public CompletionStage<ProgramomrademedlemskapResource> programomrademedlemskap(
            @Argument("systemId") String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Programomrademedlemskap");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getProgramomrademedlemskapResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<ProgramomrademedlemskapResource>empty().toFuture();
    }
}
