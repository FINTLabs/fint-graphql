// Built from tag master

package no.fint.graphql.model.utdanning.programomrade;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import no.fint.model.resource.utdanning.utdanningsprogram.ProgramomradeResource;
import no.fint.model.resource.utdanning.utdanningsprogram.ProgramomradeResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("utdanningProgramomradeQueryResolver")
public class ProgramomradeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private ProgramomradeService service;

    public List<ProgramomradeResource> getProgramomrade(String sinceTimeStamp) {
        ProgramomradeResources resources = service.getProgramomradeResources(sinceTimeStamp);
        return resources.getContent();
    }
}
