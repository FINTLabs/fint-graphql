
package no.fint.graphql.model.utdanning.larling;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.larling.LarlingResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningLarlingQueryResolver")
public class LarlingQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private LarlingService service;

    public CompletionStage<LarlingResource> getLarling(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getLarlingResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<LarlingResource>empty().toFuture();
    }
}
