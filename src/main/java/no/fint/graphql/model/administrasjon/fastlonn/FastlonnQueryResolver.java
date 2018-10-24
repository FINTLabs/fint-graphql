// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.fastlonn;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import no.fint.model.resource.administrasjon.personal.FastlonnResource;
import no.fint.model.resource.administrasjon.personal.FastlonnResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FastlonnQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private FastlonnService service;

    public List<FastlonnResource> getFastlonn(String sinceTimeStamp) {
        FastlonnResources resources = service.getFastlonnResources(sinceTimeStamp);
        return resources.getContent();
    }
}
