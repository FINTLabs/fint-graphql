
package no.fint.graphql.model.utdanning.elevforhold;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.elev.ElevforholdResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("utdanningElevforholdQueryResolver")
public class ElevforholdQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private ElevforholdService service;

    public ElevforholdResource getElevforhold(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getElevforholdResourceById("systemid", systemId, dfe).block();
        }
        return null;
    }
}
