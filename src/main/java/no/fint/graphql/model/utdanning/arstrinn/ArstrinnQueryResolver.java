// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.arstrinn;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.utdanningsprogram.ArstrinnResource;
import no.fint.model.resource.utdanning.utdanningsprogram.ArstrinnResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("utdanningArstrinnQueryResolver")
public class ArstrinnQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private ArstrinnService service;

    public List<ArstrinnResource> getArstrinn(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        ArstrinnResources resources = service.getArstrinnResources(sinceTimeStamp, dfe);
        return resources.getContent();
    }
}
