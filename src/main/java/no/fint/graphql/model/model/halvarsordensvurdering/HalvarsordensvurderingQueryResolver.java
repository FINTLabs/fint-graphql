
package no.fint.graphql.model.model.halvarsordensvurdering;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.vurdering.HalvarsordensvurderingResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelHalvarsordensvurderingQueryResolver")
@Slf4j
public class HalvarsordensvurderingQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private HalvarsordensvurderingService service;

    public CompletionStage<HalvarsordensvurderingResource> getHalvarsordensvurdering(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Halvarsordensvurdering");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getHalvarsordensvurderingResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<HalvarsordensvurderingResource>empty().toFuture();
    }
}
