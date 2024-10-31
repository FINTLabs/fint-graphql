
package no.fint.graphql.model.utdanning.avlagtprove;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.larling.AvlagtProveResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningAvlagtProveQueryResolver")
public class AvlagtProveQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private AvlagtProveService service;

    public CompletionStage<AvlagtProveResource> getAvlagtprove(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getAvlagtProveResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<AvlagtProveResource>empty().toFuture();
    }
}
