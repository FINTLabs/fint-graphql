// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.rom;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import no.fint.model.resource.utdanning.timeplan.RomResource;
import no.fint.model.resource.utdanning.timeplan.RomResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("utdanningRomQueryResolver")
public class RomQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private RomService service;

    public List<RomResource> getRom(String sinceTimeStamp) {
        RomResources resources = service.getRomResources(sinceTimeStamp);
        return resources.getContent();
    }
}
