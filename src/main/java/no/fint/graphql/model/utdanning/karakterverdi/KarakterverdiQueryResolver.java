
package no.fint.graphql.model.utdanning.karakterverdi;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.vurdering.KarakterverdiResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("utdanningKarakterverdiQueryResolver")
public class KarakterverdiQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private KarakterverdiService service;

    public KarakterverdiResource getKarakterverdi(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getKarakterverdiResourceById("systemid", systemId, dfe);
        }
        return null;
    }
}
