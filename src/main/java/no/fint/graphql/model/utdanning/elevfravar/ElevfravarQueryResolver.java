
package no.fint.graphql.model.utdanning.elevfravar;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.utdanning.vurdering.ElevfravarResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningElevfravarQueryResolver")
@Slf4j
public class ElevfravarQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private ElevfravarService service;

    public CompletionStage<ElevfravarResource> getElevfravar(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Elevfravar");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getElevfravarResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<ElevfravarResource>empty().toFuture();
    }
}
