// Built from tag release-3.2

package no.fint.graphql.model.administrasjon.fravarstype;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.administrasjon.kodeverk.FravarstypeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("administrasjonFravarstypeQueryResolver")
public class FravarstypeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private FravarstypeService service;

    public FravarstypeResource getFravarstype(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getFravarstypeResourceById("systemid", systemId, dfe);
        }
        return null;
    }
}
