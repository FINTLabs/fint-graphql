// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.skoleressurs;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import no.fint.model.resource.utdanning.elev.SkoleressursResource;
import no.fint.model.resource.utdanning.elev.SkoleressursResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SkoleressursQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private SkoleressursService service;

    public List<SkoleressursResource> getSkoleressurs(String sinceTimeStamp) {
        SkoleressursResources resources = service.getSkoleressursResources(sinceTimeStamp);
        return resources.getContent();
    }
}
