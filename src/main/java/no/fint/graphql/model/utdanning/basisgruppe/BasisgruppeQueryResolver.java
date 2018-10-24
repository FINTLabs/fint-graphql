// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.basisgruppe;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import no.fint.model.resource.utdanning.elev.BasisgruppeResource;
import no.fint.model.resource.utdanning.elev.BasisgruppeResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BasisgruppeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private BasisgruppeService service;

    public List<BasisgruppeResource> getBasisgruppe(String sinceTimeStamp) {
        BasisgruppeResources resources = service.getBasisgruppeResources(sinceTimeStamp);
        return resources.getContent();
    }
}
