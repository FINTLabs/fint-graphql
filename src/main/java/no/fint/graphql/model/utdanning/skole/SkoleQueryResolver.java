// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.skole;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("utdanningSkoleQueryResolver")
public class SkoleQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private SkoleService service;

    public List<SkoleResource> getSkole(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        SkoleResources resources = service.getSkoleResources(sinceTimeStamp, dfe);
        return resources.getContent();
    }
}
