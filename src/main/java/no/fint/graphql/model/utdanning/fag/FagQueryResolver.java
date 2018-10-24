// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.fag;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import no.fint.model.resource.utdanning.timeplan.FagResource;
import no.fint.model.resource.utdanning.timeplan.FagResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FagQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private FagService service;

    public List<FagResource> getFag(String sinceTimeStamp) {
        FagResources resources = service.getFagResources(sinceTimeStamp);
        return resources.getContent();
    }
}
