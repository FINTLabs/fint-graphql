
package no.fint.graphql.model.utdanning.elevvurdering;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.vurdering.ElevvurderingResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningElevvurderingQueryResolver")
public class ElevvurderingQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private ElevvurderingService service;

    public CompletionStage<ElevvurderingResource> getElevvurdering(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getElevvurderingResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<ElevvurderingResource>empty().toFuture();
    }
}
