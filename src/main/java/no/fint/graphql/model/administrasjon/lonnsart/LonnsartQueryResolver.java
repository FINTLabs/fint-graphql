// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.lonnsart;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.administrasjon.kodeverk.LonnsartResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("administrasjonLonnsartQueryResolver")
public class LonnsartQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private LonnsartService service;

    public LonnsartResource getLonnsart(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getLonnsartResourceById("systemid", systemId, dfe);
        }
        return null;
    }
}
