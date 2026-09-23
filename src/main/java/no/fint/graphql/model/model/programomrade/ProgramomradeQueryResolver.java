
package no.fint.graphql.model.model.programomrade;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.utdanningsprogram.ProgramomradeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelProgramomradeQueryResolver")
@Slf4j
public class ProgramomradeQueryResolver {

    @Autowired
    private ProgramomradeService service;

    @QueryMapping(name = "programomrade")
    public CompletionStage<ProgramomradeResource> programomrade(
            @Argument("systemId") String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Programomrade");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getProgramomradeResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<ProgramomradeResource>empty().toFuture();
    }
}
