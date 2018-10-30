// Built from tag master

package no.fint.graphql.model.felles.kjonn;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import no.fint.model.resource.felles.kodeverk.iso.KjonnResource;
import no.fint.model.resource.felles.kodeverk.iso.KjonnResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("fellesKjonnQueryResolver")
public class KjonnQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private KjonnService service;

    public List<KjonnResource> getKjonn(String sinceTimeStamp) {
        KjonnResources resources = service.getKjonnResources(sinceTimeStamp);
        return resources.getContent();
    }
}
