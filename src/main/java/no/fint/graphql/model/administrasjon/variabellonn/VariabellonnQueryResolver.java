// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.variabellonn;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import no.fint.model.resource.administrasjon.personal.VariabellonnResource;
import no.fint.model.resource.administrasjon.personal.VariabellonnResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("administrasjonVariabellonnQueryResolver")
public class VariabellonnQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private VariabellonnService service;

    public List<VariabellonnResource> getVariabellonn(String sinceTimeStamp) {
        VariabellonnResources resources = service.getVariabellonnResources(sinceTimeStamp);
        return resources.getContent();
    }
}
