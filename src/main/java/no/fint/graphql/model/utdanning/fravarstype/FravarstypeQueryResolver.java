// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.fravarstype;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import no.fint.model.resource.utdanning.kodeverk.FravarstypeResource;
import no.fint.model.resource.utdanning.kodeverk.FravarstypeResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("utdanningFravarstypeQueryResolver")
public class FravarstypeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private FravarstypeService service;

    public List<FravarstypeResource> getFravarstype(String sinceTimeStamp) {
        FravarstypeResources resources = service.getFravarstypeResources(sinceTimeStamp);
        return resources.getContent();
    }
}
