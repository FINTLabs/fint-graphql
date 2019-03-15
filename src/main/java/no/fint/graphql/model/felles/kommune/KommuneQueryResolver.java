
package no.fint.graphql.model.felles.kommune;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.felles.kodeverk.KommuneResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("fellesKommuneQueryResolver")
public class KommuneQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private KommuneService service;

    public KommuneResource getKommune(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getKommuneResourceById("systemid", systemId, dfe);
        }
        return null;
    }
}
