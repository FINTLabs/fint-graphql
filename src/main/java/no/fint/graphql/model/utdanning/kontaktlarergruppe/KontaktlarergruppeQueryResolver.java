// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.kontaktlarergruppe;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.elev.KontaktlarergruppeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("utdanningKontaktlarergruppeQueryResolver")
public class KontaktlarergruppeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private KontaktlarergruppeService service;

    public KontaktlarergruppeResource getKontaktlarergruppe(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getKontaktlarergruppeResourceById("systemid", systemId, dfe);
        }
        return null;
    }
}
