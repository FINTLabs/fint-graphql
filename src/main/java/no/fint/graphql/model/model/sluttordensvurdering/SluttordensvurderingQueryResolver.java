
package no.fint.graphql.model.model.sluttordensvurdering;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.vurdering.SluttordensvurderingResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelSluttordensvurderingQueryResolver")
@Slf4j
public class SluttordensvurderingQueryResolver {

    @Autowired
    private SluttordensvurderingService service;

    @QueryMapping(name = "sluttordensvurdering")
    public CompletionStage<SluttordensvurderingResource> sluttordensvurdering(
            @Argument("systemId") String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Sluttordensvurdering");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getSluttordensvurderingResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<SluttordensvurderingResource>empty().toFuture();
    }
}
