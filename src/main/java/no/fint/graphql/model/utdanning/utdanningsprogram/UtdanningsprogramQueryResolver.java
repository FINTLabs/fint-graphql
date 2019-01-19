// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.utdanningsprogram;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.utdanningsprogram.UtdanningsprogramResource;
import no.fint.model.resource.utdanning.utdanningsprogram.UtdanningsprogramResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("utdanningUtdanningsprogramQueryResolver")
public class UtdanningsprogramQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private UtdanningsprogramService service;

    public List<UtdanningsprogramResource> getUtdanningsprogram(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        UtdanningsprogramResources resources = service.getUtdanningsprogramResources(sinceTimeStamp, dfe);
        return resources.getContent();
    }
}
