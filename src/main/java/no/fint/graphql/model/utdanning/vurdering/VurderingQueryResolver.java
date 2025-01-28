
package no.fint.graphql.model.utdanning.vurdering;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.utdanning.vurdering.VurderingResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningVurderingQueryResolver")
@Slf4j
public class VurderingQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private VurderingService service;

    public CompletionStage<VurderingResource> getVurdering(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Vurdering");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getVurderingResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<VurderingResource>empty().toFuture();
    }
}
