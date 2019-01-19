// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.utdanningsprogram;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.utdanningsprogram.UtdanningsprogramResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("utdanningUtdanningsprogramQueryResolver")
public class UtdanningsprogramQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private UtdanningsprogramService service;

    public UtdanningsprogramResource getUtdanningsprogram(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getUtdanningsprogramResourceById("systemid", systemId, dfe);
        }
        return null;
    }
}
