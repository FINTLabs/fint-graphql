
package no.fint.graphql.model.model.larling;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.larling.LarlingResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelLarlingQueryResolver")
@Slf4j
public class LarlingQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private LarlingService service;

    public CompletionStage<LarlingResource> getLarling(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Larling");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getLarlingResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<LarlingResource>empty().toFuture();
    }
}
