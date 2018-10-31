// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.eksamensgruppe;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import no.fint.model.resource.utdanning.vurdering.EksamensgruppeResource;
import no.fint.model.resource.utdanning.vurdering.EksamensgruppeResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("utdanningEksamensgruppeQueryResolver")
public class EksamensgruppeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private EksamensgruppeService service;

    public List<EksamensgruppeResource> getEksamensgruppe(String sinceTimeStamp) {
        EksamensgruppeResources resources = service.getEksamensgruppeResources(sinceTimeStamp);
        return resources.getContent();
    }
}
