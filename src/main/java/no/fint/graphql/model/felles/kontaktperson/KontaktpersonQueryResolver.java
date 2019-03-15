// Built from tag release-3.2

package no.fint.graphql.model.felles.kontaktperson;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.felles.KontaktpersonResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("fellesKontaktpersonQueryResolver")
public class KontaktpersonQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private KontaktpersonService service;

    public KontaktpersonResource getKontaktperson(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getKontaktpersonResourceById("systemid", systemId, dfe);
        }
        return null;
    }
}
