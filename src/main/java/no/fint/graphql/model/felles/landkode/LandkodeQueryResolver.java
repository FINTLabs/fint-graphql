// Built from tag release-3.2

package no.fint.graphql.model.felles.landkode;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.felles.kodeverk.iso.LandkodeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("fellesLandkodeQueryResolver")
public class LandkodeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private LandkodeService service;

    public LandkodeResource getLandkode(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getLandkodeResourceById("systemid", systemId, dfe);
        }
        return null;
    }
}
