
package no.fint.graphql.model.utdanning.sluttfagvurdering;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.vurdering.SluttfagvurderingResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningSluttfagvurderingQueryResolver")
public class SluttfagvurderingQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private SluttfagvurderingService service;

    public CompletionStage<SluttfagvurderingResource> getSluttfagvurdering(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getSluttfagvurderingResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<SluttfagvurderingResource>empty().toFuture();
    }
}
