// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.variabellonn;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.administrasjon.personal.VariabellonnResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("administrasjonVariabellonnQueryResolver")
public class VariabellonnQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private VariabellonnService service;

    public VariabellonnResource getVariabellonn(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getVariabellonnResourceById("systemid", systemId, dfe);
        }
        return null;
    }
}
