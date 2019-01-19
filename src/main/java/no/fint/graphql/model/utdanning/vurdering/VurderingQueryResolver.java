// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.vurdering;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.vurdering.VurderingResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("utdanningVurderingQueryResolver")
public class VurderingQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private VurderingService service;

    public VurderingResource getVurdering(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getVurderingResourceById("systemid", systemId, dfe);
        }
        return null;
    }
}
