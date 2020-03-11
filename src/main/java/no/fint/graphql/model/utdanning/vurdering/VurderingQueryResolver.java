
package no.fint.graphql.model.utdanning.vurdering;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.vurdering.VurderingResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningVurderingQueryResolver")
public class VurderingQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private VurderingService service;

    public CompletionStage<VurderingResource> getVurdering(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getVurderingResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<VurderingResource>empty().toFuture();
    }
}
