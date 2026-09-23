
package no.fint.graphql.model.model.larling;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.larling.LarlingResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelLarlingQueryResolver")
@Slf4j
public class LarlingQueryResolver {

    @Autowired
    private LarlingService service;

    @QueryMapping(name = "larling")
    public CompletionStage<LarlingResource> larling(
            @Argument("systemId") String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Larling");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getLarlingResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<LarlingResource>empty().toFuture();
    }
}
