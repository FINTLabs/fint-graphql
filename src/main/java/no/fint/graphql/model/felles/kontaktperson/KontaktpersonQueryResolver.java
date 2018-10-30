// Built from tag master

package no.fint.graphql.model.felles.kontaktperson;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import no.fint.model.resource.felles.KontaktpersonResource;
import no.fint.model.resource.felles.KontaktpersonResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("fellesKontaktpersonQueryResolver")
public class KontaktpersonQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private KontaktpersonService service;

    public List<KontaktpersonResource> getKontaktperson(String sinceTimeStamp) {
        KontaktpersonResources resources = service.getKontaktpersonResources(sinceTimeStamp);
        return resources.getContent();
    }
}
