
package no.fint.graphql.model.utdanning.sluttordensvurdering;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.vurdering.SluttordensvurderingResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningSluttordensvurderingQueryResolver")
public class SluttordensvurderingQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private SluttordensvurderingService service;

    public CompletionStage<SluttordensvurderingResource> getSluttordensvurdering(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getSluttordensvurderingResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<SluttordensvurderingResource>empty().toFuture();
    }
}
