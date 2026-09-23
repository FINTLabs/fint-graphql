
package no.fint.graphql.model.model.elevvurdering;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.vurdering.ElevvurderingResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelElevvurderingQueryResolver")
@Slf4j
public class ElevvurderingQueryResolver {

    @Autowired
    private ElevvurderingService service;

    @QueryMapping(name = "elevvurdering")
    public CompletionStage<ElevvurderingResource> elevvurdering(
            @Argument("systemId") String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Elevvurdering");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getElevvurderingResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<ElevvurderingResource>empty().toFuture();
    }
}
