// Built from tag release-3.2

package no.fint.graphql.model.felles.sprak;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.felles.kodeverk.iso.SprakResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("fellesSprakQueryResolver")
public class SprakQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private SprakService service;

    public SprakResource getSprak(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getSprakResourceById("systemid", systemId, dfe);
        }
        return null;
    }
}
