// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.fravarstype;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import no.fint.model.resource.administrasjon.kodeverk.FravarstypeResource;
import no.fint.model.resource.administrasjon.kodeverk.FravarstypeResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FravarstypeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private FravarstypeService service;

    public List<FravarstypeResource> getFravarstype(String sinceTimeStamp) {
        FravarstypeResources resources = service.getFravarstypeResources(sinceTimeStamp);
        return resources.getContent();
    }
}
