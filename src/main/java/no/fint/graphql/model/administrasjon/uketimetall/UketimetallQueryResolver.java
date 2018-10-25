// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.uketimetall;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import no.fint.model.resource.administrasjon.kodeverk.UketimetallResource;
import no.fint.model.resource.administrasjon.kodeverk.UketimetallResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("administrasjonUketimetallQueryResolver")
public class UketimetallQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private UketimetallService service;

    public List<UketimetallResource> getUketimetall(String sinceTimeStamp) {
        UketimetallResources resources = service.getUketimetallResources(sinceTimeStamp);
        return resources.getContent();
    }
}
