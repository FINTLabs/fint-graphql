
package no.fint.graphql.model.model.avlagtprove;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.larling.AvlagtProveResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelAvlagtProveQueryResolver")
@Slf4j
public class AvlagtProveQueryResolver {

    @Autowired
    private AvlagtProveService service;

    @QueryMapping(name = "avlagtprove")
    public CompletionStage<AvlagtProveResource> avlagtprove(
            @Argument("systemId") String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for AvlagtProve");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getAvlagtProveResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<AvlagtProveResource>empty().toFuture();
    }
}
