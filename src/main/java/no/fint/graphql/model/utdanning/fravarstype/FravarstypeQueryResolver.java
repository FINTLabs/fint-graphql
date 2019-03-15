// Built from tag release-3.2

package no.fint.graphql.model.utdanning.fravarstype;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.kodeverk.FravarstypeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("utdanningFravarstypeQueryResolver")
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
