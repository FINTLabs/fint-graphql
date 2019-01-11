// Built from tag v3.1.0

package no.fint.graphql.model.felles.landkode;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.felles.kodeverk.iso.LandkodeResource;
import no.fint.model.resource.felles.kodeverk.iso.LandkodeResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("fellesLandkodeQueryResolver")
public class LandkodeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private LandkodeService service;

    public List<LandkodeResource> getLandkode(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        LandkodeResources resources = service.getLandkodeResources(sinceTimeStamp, dfe);
        return resources.getContent();
    }
}
