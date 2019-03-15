// Built from tag release-3.2

package no.fint.graphql.model.administrasjon.fastlonn;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.administrasjon.personal.FastlonnResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("administrasjonFastlonnQueryResolver")
public class FastlonnQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private FastlonnService service;

    public FastlonnResource getFastlonn(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getFastlonnResourceById("systemid", systemId, dfe);
        }
        return null;
    }
}
