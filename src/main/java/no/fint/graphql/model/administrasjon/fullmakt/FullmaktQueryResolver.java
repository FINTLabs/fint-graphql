
package no.fint.graphql.model.administrasjon.fullmakt;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.administrasjon.fullmakt.FullmaktResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("administrasjonFullmaktQueryResolver")
public class FullmaktQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private FullmaktService service;

    public FullmaktResource getFullmakt(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getFullmaktResourceById("systemid", systemId, dfe);
        }
        return null;
    }
}
