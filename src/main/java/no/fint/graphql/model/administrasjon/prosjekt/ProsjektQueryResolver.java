// Built from tag master

package no.fint.graphql.model.administrasjon.prosjekt;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import no.fint.model.resource.administrasjon.kodeverk.ProsjektResource;
import no.fint.model.resource.administrasjon.kodeverk.ProsjektResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("administrasjonProsjektQueryResolver")
public class ProsjektQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private ProsjektService service;

    public List<ProsjektResource> getProsjekt(String sinceTimeStamp) {
        ProsjektResources resources = service.getProsjektResources(sinceTimeStamp);
        return resources.getContent();
    }
}
