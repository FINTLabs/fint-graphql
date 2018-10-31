// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.elev;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import no.fint.model.resource.utdanning.elev.ElevResource;
import no.fint.model.resource.utdanning.elev.ElevResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("utdanningElevQueryResolver")
public class ElevQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private ElevService service;

    public List<ElevResource> getElev(String sinceTimeStamp) {
        ElevResources resources = service.getElevResources(sinceTimeStamp);
        return resources.getContent();
    }
}
