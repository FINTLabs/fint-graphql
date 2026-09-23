
package no.fint.graphql.model.model.underveisordensvurdering;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.vurdering.UnderveisordensvurderingResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelUnderveisordensvurderingQueryResolver")
@Slf4j
public class UnderveisordensvurderingQueryResolver {

    @Autowired
    private UnderveisordensvurderingService service;

    @QueryMapping(name = "underveisordensvurdering")
    public CompletionStage<UnderveisordensvurderingResource> underveisordensvurdering(
            @Argument("systemId") String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Underveisordensvurdering");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getUnderveisordensvurderingResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<UnderveisordensvurderingResource>empty().toFuture();
    }
}
