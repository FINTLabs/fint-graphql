
package no.fint.graphql.model.model.underveisordensvurdering;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.vurdering.UnderveisordensvurderingResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelUnderveisordensvurderingQueryResolver")
@Slf4j
public class UnderveisordensvurderingQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private UnderveisordensvurderingService service;

    public CompletionStage<UnderveisordensvurderingResource> getUnderveisordensvurdering(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Underveisordensvurdering");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getUnderveisordensvurderingResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<UnderveisordensvurderingResource>empty().toFuture();
    }
}
