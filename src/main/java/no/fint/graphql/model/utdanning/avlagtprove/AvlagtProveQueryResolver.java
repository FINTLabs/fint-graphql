
package no.fint.graphql.model.utdanning.avlagtprove;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.utdanning.larling.AvlagtProveResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningAvlagtProveQueryResolver")
@Slf4j
public class AvlagtProveQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private AvlagtProveService service;

    public CompletionStage<AvlagtProveResource> getAvlagtProve(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for AvlagtProve");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getAvlagtProveResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<AvlagtProveResource>empty().toFuture();
    }
}
