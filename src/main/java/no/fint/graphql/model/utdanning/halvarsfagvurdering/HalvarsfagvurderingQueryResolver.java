
package no.fint.graphql.model.utdanning.halvarsfagvurdering;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.vurdering.HalvarsfagvurderingResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningHalvarsfagvurderingQueryResolver")
public class HalvarsfagvurderingQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private HalvarsfagvurderingService service;

    public CompletionStage<HalvarsfagvurderingResource> getHalvarsfagvurdering(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getHalvarsfagvurderingResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<HalvarsfagvurderingResource>empty().toFuture();
    }
}
