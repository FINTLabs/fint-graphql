
package no.fint.graphql.model.model.sluttfagvurdering;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.vurdering.SluttfagvurderingResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelSluttfagvurderingQueryResolver")
@Slf4j
public class SluttfagvurderingQueryResolver {

    @Autowired
    private SluttfagvurderingService service;

    @QueryMapping(name = "sluttfagvurdering")
    public CompletionStage<SluttfagvurderingResource> sluttfagvurdering(
            @Argument("systemId") String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Sluttfagvurdering");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getSluttfagvurderingResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<SluttfagvurderingResource>empty().toFuture();
    }
}
