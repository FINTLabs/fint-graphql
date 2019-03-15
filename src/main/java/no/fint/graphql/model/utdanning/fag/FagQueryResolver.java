
package no.fint.graphql.model.utdanning.fag;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.timeplan.FagResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("utdanningFagQueryResolver")
public class FagQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private FagService service;

    public FagResource getFag(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getFagResourceById("systemid", systemId, dfe);
        }
        return null;
    }
}
