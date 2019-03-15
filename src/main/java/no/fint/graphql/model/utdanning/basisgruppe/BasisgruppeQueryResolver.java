// Built from tag release-3.2

package no.fint.graphql.model.utdanning.basisgruppe;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.elev.BasisgruppeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("utdanningBasisgruppeQueryResolver")
public class BasisgruppeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private BasisgruppeService service;

    public BasisgruppeResource getBasisgruppe(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getBasisgruppeResourceById("systemid", systemId, dfe);
        }
        return null;
    }
}
