
package no.fint.graphql.model.administrasjon.personalressurs;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("administrasjonPersonalressursQueryResolver")
public class PersonalressursQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private PersonalressursService service;

    public PersonalressursResource getPersonalressurs(
            String ansattnummer,
            String brukernavn,
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(ansattnummer)) {
            return service.getPersonalressursResourceById("ansattnummer", ansattnummer, dfe);
        }
        if (StringUtils.isNotEmpty(brukernavn)) {
            return service.getPersonalressursResourceById("brukernavn", brukernavn, dfe);
        }
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getPersonalressursResourceById("systemid", systemId, dfe);
        }
        return null;
    }
}
