// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.arstrinn;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import no.fint.model.resource.utdanning.utdanningsprogram.ArstrinnResource;
import no.fint.model.resource.utdanning.utdanningsprogram.ArstrinnResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("utdanningArstrinnQueryResolver")
public class ArstrinnQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private ArstrinnService service;

    public List<ArstrinnResource> getArstrinn(String sinceTimeStamp) {
        ArstrinnResources resources = service.getArstrinnResources(sinceTimeStamp);
        return resources.getContent();
    }
}
