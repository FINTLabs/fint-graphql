// Built from tag master

package no.fint.graphql.model.administrasjon.rolle;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import no.fint.model.resource.administrasjon.fullmakt.RolleResource;
import no.fint.model.resource.administrasjon.fullmakt.RolleResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("administrasjonRolleQueryResolver")
public class RolleQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private RolleService service;

    public List<RolleResource> getRolle(String sinceTimeStamp) {
        RolleResources resources = service.getRolleResources(sinceTimeStamp);
        return resources.getContent();
    }
}
