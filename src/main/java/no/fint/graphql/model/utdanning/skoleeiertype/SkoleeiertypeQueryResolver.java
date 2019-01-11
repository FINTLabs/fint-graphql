// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.skoleeiertype;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.kodeverk.SkoleeiertypeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("utdanningSkoleeiertypeQueryResolver")
public class SkoleeiertypeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private SkoleeiertypeService service;

    public SkoleeiertypeResource getSkoleeiertype(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getSkoleeiertypeResourceById("systemid", systemId, dfe);
        }
        return null;
    }
}
