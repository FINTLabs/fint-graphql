// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.personalressurs;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.administrasjon.personal.PersonalressursResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("administrasjonPersonalressursQueryResolver")
public class PersonalressursQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private PersonalressursService service;

    public List<PersonalressursResource> getPersonalressurs(String sinceTimeStamp) {
        PersonalressursResources resources = service.getPersonalressursResources(sinceTimeStamp);
        return resources.getContent();
    }
}
