// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.prosjekt;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.administrasjon.kodeverk.ProsjektResource;
import no.fint.model.resource.administrasjon.kodeverk.ProsjektResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("administrasjonProsjektQueryResolver")
public class ProsjektQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private ProsjektService service;

    public List<ProsjektResource> getProsjekt(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        ProsjektResources resources = service.getProsjektResources(sinceTimeStamp, dfe);
        return resources.getContent();
    }
}
