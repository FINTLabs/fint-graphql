// Built from tag master

package no.fint.graphql.model.administrasjon.lonnsart;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import no.fint.model.resource.administrasjon.kodeverk.LonnsartResource;
import no.fint.model.resource.administrasjon.kodeverk.LonnsartResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("administrasjonLonnsartQueryResolver")
public class LonnsartQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private LonnsartService service;

    public List<LonnsartResource> getLonnsart(String sinceTimeStamp) {
        LonnsartResources resources = service.getLonnsartResources(sinceTimeStamp);
        return resources.getContent();
    }
}
