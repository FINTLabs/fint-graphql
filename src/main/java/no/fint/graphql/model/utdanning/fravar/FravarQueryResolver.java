// Built from tag release-3.2

package no.fint.graphql.model.utdanning.fravar;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.vurdering.FravarResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("utdanningFravarQueryResolver")
public class FravarQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private FravarService service;

    public FravarResource getFravar(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getFravarResourceById("systemid", systemId, dfe);
        }
        return null;
    }
}
