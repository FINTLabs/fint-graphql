// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.fullmakt;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import no.fint.model.resource.administrasjon.fullmakt.FullmaktResource;
import no.fint.model.resource.administrasjon.fullmakt.FullmaktResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FullmaktQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private FullmaktService service;

    public List<FullmaktResource> getFullmakt(String sinceTimeStamp) {
        FullmaktResources resources = service.getFullmaktResources(sinceTimeStamp);
        return resources.getContent();
    }
}
