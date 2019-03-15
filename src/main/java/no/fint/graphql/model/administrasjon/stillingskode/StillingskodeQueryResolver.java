
package no.fint.graphql.model.administrasjon.stillingskode;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.administrasjon.kodeverk.StillingskodeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("administrasjonStillingskodeQueryResolver")
public class StillingskodeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private StillingskodeService service;

    public StillingskodeResource getStillingskode(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getStillingskodeResourceById("systemid", systemId, dfe);
        }
        return null;
    }
}
