// Built from tag release-3.2

package no.fint.graphql.model.administrasjon.personalressurskategori;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.administrasjon.kodeverk.PersonalressurskategoriResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("administrasjonPersonalressurskategoriQueryResolver")
public class PersonalressurskategoriQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private PersonalressurskategoriService service;

    public PersonalressurskategoriResource getPersonalressurskategori(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getPersonalressurskategoriResourceById("systemid", systemId, dfe);
        }
        return null;
    }
}
