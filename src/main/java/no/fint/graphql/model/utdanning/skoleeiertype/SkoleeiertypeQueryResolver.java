// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.skoleeiertype;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import no.fint.model.resource.utdanning.kodeverk.SkoleeiertypeResource;
import no.fint.model.resource.utdanning.kodeverk.SkoleeiertypeResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("utdanningSkoleeiertypeQueryResolver")
public class SkoleeiertypeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private SkoleeiertypeService service;

    public List<SkoleeiertypeResource> getSkoleeiertype(String sinceTimeStamp) {
        SkoleeiertypeResources resources = service.getSkoleeiertypeResources(sinceTimeStamp);
        return resources.getContent();
    }
}
