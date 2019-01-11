// Built from tag v3.1.0

package no.fint.graphql.model.felles.kjonn;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.felles.kodeverk.iso.KjonnResource;
import no.fint.model.resource.felles.kodeverk.iso.KjonnResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("fellesKjonnQueryResolver")
public class KjonnQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private KjonnService service;

    public List<KjonnResource> getKjonn(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        KjonnResources resources = service.getKjonnResources(sinceTimeStamp, dfe);
        return resources.getContent();
    }
}
