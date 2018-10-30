// Built from tag master

package no.fint.graphql.model.utdanning.elevforhold;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import no.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.fint.model.resource.utdanning.elev.ElevforholdResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("utdanningElevforholdQueryResolver")
public class ElevforholdQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private ElevforholdService service;

    public List<ElevforholdResource> getElevforhold(String sinceTimeStamp) {
        ElevforholdResources resources = service.getElevforholdResources(sinceTimeStamp);
        return resources.getContent();
    }
}
