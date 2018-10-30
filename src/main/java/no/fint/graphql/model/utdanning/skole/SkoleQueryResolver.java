// Built from tag master

package no.fint.graphql.model.utdanning.skole;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("utdanningSkoleQueryResolver")
public class SkoleQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private SkoleService service;

    public List<SkoleResource> getSkole(String sinceTimeStamp) {
        SkoleResources resources = service.getSkoleResources(sinceTimeStamp);
        return resources.getContent();
    }
}
