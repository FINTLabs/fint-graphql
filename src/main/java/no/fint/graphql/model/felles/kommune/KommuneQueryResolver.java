// Built from tag v3.1.0

package no.fint.graphql.model.felles.kommune;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.felles.kodeverk.KommuneResource;
import no.fint.model.resource.felles.kodeverk.KommuneResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("fellesKommuneQueryResolver")
public class KommuneQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private KommuneService service;

    public List<KommuneResource> getKommune(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        KommuneResources resources = service.getKommuneResources(sinceTimeStamp, dfe);
        return resources.getContent();
    }
}
