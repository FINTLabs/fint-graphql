
package no.fint.graphql.model.utdanning.eksamensvurdering;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.utdanning.vurdering.EksamensvurderingResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningEksamensvurderingQueryResolver")
@Slf4j
public class EksamensvurderingQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private EksamensvurderingService service;

    public CompletionStage<EksamensvurderingResource> getEksamensvurdering(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Eksamensvurdering");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getEksamensvurderingResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<EksamensvurderingResource>empty().toFuture();
    }
}
