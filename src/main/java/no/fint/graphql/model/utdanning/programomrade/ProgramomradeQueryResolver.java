// Built from tag release-3.2

package no.fint.graphql.model.utdanning.programomrade;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.utdanningsprogram.ProgramomradeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("utdanningProgramomradeQueryResolver")
public class ProgramomradeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private ProgramomradeService service;

    public ProgramomradeResource getProgramomrade(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getProgramomradeResourceById("systemid", systemId, dfe);
        }
        return null;
    }
}
