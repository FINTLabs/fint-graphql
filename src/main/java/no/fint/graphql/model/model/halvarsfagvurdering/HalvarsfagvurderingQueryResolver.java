
package no.fint.graphql.model.model.halvarsfagvurdering;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.vurdering.HalvarsfagvurderingResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelHalvarsfagvurderingQueryResolver")
@Slf4j
public class HalvarsfagvurderingQueryResolver {

    @Autowired
    private HalvarsfagvurderingService service;

    @QueryMapping(name = "halvarsfagvurdering")
    public CompletionStage<HalvarsfagvurderingResource> halvarsfagvurdering(
            @Argument("systemId") String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Halvarsfagvurdering");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getHalvarsfagvurderingResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<HalvarsfagvurderingResource>empty().toFuture();
    }
}
