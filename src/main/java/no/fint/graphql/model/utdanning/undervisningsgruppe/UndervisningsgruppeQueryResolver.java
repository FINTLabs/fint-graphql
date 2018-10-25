// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.undervisningsgruppe;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import no.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource;
import no.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("utdanningUndervisningsgruppeQueryResolver")
public class UndervisningsgruppeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private UndervisningsgruppeService service;

    public List<UndervisningsgruppeResource> getUndervisningsgruppe(String sinceTimeStamp) {
        UndervisningsgruppeResources resources = service.getUndervisningsgruppeResources(sinceTimeStamp);
        return resources.getContent();
    }
}
