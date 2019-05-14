
package no.fint.graphql.model.administrasjon.rolle;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.administrasjon.fullmakt.RolleResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("administrasjonRolleQueryResolver")
public class RolleQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private RolleService service;

    public RolleResource getRolle(
            String navn,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(navn)) {
            return service.getRolleResourceById("navn", navn, dfe);
        }
        return null;
    }
}
