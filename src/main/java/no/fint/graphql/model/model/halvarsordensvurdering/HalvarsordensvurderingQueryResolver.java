
package no.fint.graphql.model.model.halvarsordensvurdering;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.vurdering.HalvarsordensvurderingResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelHalvarsordensvurderingQueryResolver")
@Slf4j
public class HalvarsordensvurderingQueryResolver {

    @Autowired
    private HalvarsordensvurderingService service;

    @QueryMapping(name = "halvarsordensvurdering")
    public CompletionStage<HalvarsordensvurderingResource> halvarsordensvurdering(
            @Argument("systemId") String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Halvarsordensvurdering");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getHalvarsordensvurderingResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<HalvarsordensvurderingResource>empty().toFuture();
    }
}
