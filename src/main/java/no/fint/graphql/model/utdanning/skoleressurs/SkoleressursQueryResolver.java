// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.skoleressurs;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.elev.SkoleressursResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("utdanningSkoleressursQueryResolver")
public class SkoleressursQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private SkoleressursService service;

    public SkoleressursResource getSkoleressurs(
            String feidenavn,
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(feidenavn)) {
            return service.getSkoleressursResourceById("feidenavn", feidenavn, dfe);
        }
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getSkoleressursResourceById("systemid", systemId, dfe);
        }
        return null;
    }
}
