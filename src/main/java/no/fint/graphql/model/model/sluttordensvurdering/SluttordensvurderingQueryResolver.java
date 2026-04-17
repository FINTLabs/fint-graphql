
package no.fint.graphql.model.model.sluttordensvurdering;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.vurdering.SluttordensvurderingResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelSluttordensvurderingQueryResolver")
@Slf4j
public class SluttordensvurderingQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private SluttordensvurderingService service;

    public CompletionStage<SluttordensvurderingResource> getSluttordensvurdering(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Sluttordensvurdering");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getSluttordensvurderingResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<SluttordensvurderingResource>empty().toFuture();
    }
}
