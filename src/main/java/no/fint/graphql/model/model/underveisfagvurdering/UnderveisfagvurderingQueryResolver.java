
package no.fint.graphql.model.model.underveisfagvurdering;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.vurdering.UnderveisfagvurderingResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelUnderveisfagvurderingQueryResolver")
@Slf4j
public class UnderveisfagvurderingQueryResolver {

    @Autowired
    private UnderveisfagvurderingService service;

    @QueryMapping(name = "underveisfagvurdering")
    public CompletionStage<UnderveisfagvurderingResource> underveisfagvurdering(
            @Argument("systemId") String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Underveisfagvurdering");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getUnderveisfagvurderingResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<UnderveisfagvurderingResource>empty().toFuture();
    }
}
