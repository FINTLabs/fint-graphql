
package no.fint.graphql.model.model.programomrademedlemskap;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.utdanningsprogram.ProgramomrademedlemskapResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelProgramomrademedlemskapQueryResolver")
@Slf4j
public class ProgramomrademedlemskapQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private ProgramomrademedlemskapService service;

    public CompletionStage<ProgramomrademedlemskapResource> getProgramomrademedlemskap(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Programomrademedlemskap");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getProgramomrademedlemskapResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<ProgramomrademedlemskapResource>empty().toFuture();
    }
}
