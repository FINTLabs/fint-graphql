// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.stillingskode;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import no.fint.model.resource.administrasjon.kodeverk.StillingskodeResource;
import no.fint.model.resource.administrasjon.kodeverk.StillingskodeResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("administrasjonStillingskodeQueryResolver")
public class StillingskodeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private StillingskodeService service;

    public List<StillingskodeResource> getStillingskode(String sinceTimeStamp) {
        StillingskodeResources resources = service.getStillingskodeResources(sinceTimeStamp);
        return resources.getContent();
    }
}
