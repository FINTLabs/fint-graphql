// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.kontaktlarergruppe;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import no.fint.model.resource.utdanning.elev.KontaktlarergruppeResource;
import no.fint.model.resource.utdanning.elev.KontaktlarergruppeResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("utdanningKontaktlarergruppeQueryResolver")
public class KontaktlarergruppeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private KontaktlarergruppeService service;

    public List<KontaktlarergruppeResource> getKontaktlarergruppe(String sinceTimeStamp) {
        KontaktlarergruppeResources resources = service.getKontaktlarergruppeResources(sinceTimeStamp);
        return resources.getContent();
    }
}
