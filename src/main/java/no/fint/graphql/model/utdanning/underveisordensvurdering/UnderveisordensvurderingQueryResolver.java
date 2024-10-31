
package no.fint.graphql.model.utdanning.underveisordensvurdering;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.vurdering.UnderveisordensvurderingResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningUnderveisordensvurderingQueryResolver")
public class UnderveisordensvurderingQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private UnderveisordensvurderingService service;

    public CompletionStage<UnderveisordensvurderingResource> getUnderveisordensvurdering(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getUnderveisordensvurderingResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<UnderveisordensvurderingResource>empty().toFuture();
    }
}
