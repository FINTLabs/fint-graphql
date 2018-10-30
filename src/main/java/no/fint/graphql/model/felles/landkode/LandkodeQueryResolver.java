// Built from tag master

package no.fint.graphql.model.felles.landkode;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import no.fint.model.resource.felles.kodeverk.iso.LandkodeResource;
import no.fint.model.resource.felles.kodeverk.iso.LandkodeResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("fellesLandkodeQueryResolver")
public class LandkodeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private LandkodeService service;

    public List<LandkodeResource> getLandkode(String sinceTimeStamp) {
        LandkodeResources resources = service.getLandkodeResources(sinceTimeStamp);
        return resources.getContent();
    }
}
