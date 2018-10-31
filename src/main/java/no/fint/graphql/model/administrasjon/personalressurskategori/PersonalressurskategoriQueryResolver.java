// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.personalressurskategori;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import no.fint.model.resource.administrasjon.kodeverk.PersonalressurskategoriResource;
import no.fint.model.resource.administrasjon.kodeverk.PersonalressurskategoriResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("administrasjonPersonalressurskategoriQueryResolver")
public class PersonalressurskategoriQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private PersonalressurskategoriService service;

    public List<PersonalressurskategoriResource> getPersonalressurskategori(String sinceTimeStamp) {
        PersonalressurskategoriResources resources = service.getPersonalressurskategoriResources(sinceTimeStamp);
        return resources.getContent();
    }
}
