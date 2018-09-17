package no.fint.graphql.resolvers.personalressurs;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import no.fint.graphql.integration.personalressurs.PersonalressursService;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.administrasjon.personal.PersonalressursResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PersonalressursQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private PersonalressursService service;

    public List<PersonalressursResource> getPersonalressurs() {
        PersonalressursResources personalressurs = service.getPersonalressurs();
        return personalressurs.getContent();
    }

}
