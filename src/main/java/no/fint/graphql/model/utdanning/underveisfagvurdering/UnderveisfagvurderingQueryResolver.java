
package no.fint.graphql.model.utdanning.underveisfagvurdering;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.vurdering.UnderveisfagvurderingResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningUnderveisfagvurderingQueryResolver")
public class UnderveisfagvurderingQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private UnderveisfagvurderingService service;

    public CompletionStage<UnderveisfagvurderingResource> getUnderveisfagvurdering(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getUnderveisfagvurderingResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<UnderveisfagvurderingResource>empty().toFuture();
    }
}
