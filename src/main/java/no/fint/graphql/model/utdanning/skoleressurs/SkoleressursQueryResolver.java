// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.skoleressurs;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.elev.SkoleressursResource;
import no.fint.model.resource.utdanning.elev.SkoleressursResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("utdanningSkoleressursQueryResolver")
public class SkoleressursQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private SkoleressursService service;

    public List<SkoleressursResource> getSkoleressurs(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        SkoleressursResources resources = service.getSkoleressursResources(sinceTimeStamp, dfe);
        return resources.getContent();
    }
}
