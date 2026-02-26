
package no.fint.graphql.model.model.halvarsfagvurdering;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.vurdering.HalvarsfagvurderingResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelHalvarsfagvurderingQueryResolver")
@Slf4j
public class HalvarsfagvurderingQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private HalvarsfagvurderingService service;

    public CompletionStage<HalvarsfagvurderingResource> halvarsfagvurdering(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Halvarsfagvurdering");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getHalvarsfagvurderingResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<HalvarsfagvurderingResource>empty().toFuture();
    }
}
