package no.fint.graphql.personalressurs;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.administrasjon.personal.PersonalressursResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PersonalressursQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private PersonalressursService personalressursService;

    public List<PersonalressursResource> getPersonalressurs() {
        PersonalressursResources resources = personalressursService.getPersonalressursResources();
        return resources.getContent();
    }

}
