// Built from tag v3.1.0

package no.fint.graphql.model.felles.fylke;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.felles.kodeverk.FylkeResource;
import no.fint.model.resource.felles.kodeverk.FylkeResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("fellesFylkeQueryResolver")
public class FylkeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private FylkeService service;

    public List<FylkeResource> getFylke(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        FylkeResources resources = service.getFylkeResources(sinceTimeStamp, dfe);
        return resources.getContent();
    }
}
