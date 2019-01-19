// Built from tag v3.1.0

package no.fint.graphql.model.felles.fylke;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.felles.kodeverk.FylkeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("fellesFylkeQueryResolver")
public class FylkeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private FylkeService service;

    public FylkeResource getFylke(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getFylkeResourceById("systemid", systemId, dfe);
        }
        return null;
    }
}
