// Built from tag master

package no.fint.graphql.model.felles.fylke;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import no.fint.model.resource.felles.kodeverk.FylkeResource;
import no.fint.model.resource.felles.kodeverk.FylkeResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("fellesFylkeQueryResolver")
public class FylkeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private FylkeService service;

    public List<FylkeResource> getFylke(String sinceTimeStamp) {
        FylkeResources resources = service.getFylkeResources(sinceTimeStamp);
        return resources.getContent();
    }
}
