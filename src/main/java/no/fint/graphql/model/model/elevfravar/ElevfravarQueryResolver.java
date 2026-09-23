
package no.fint.graphql.model.model.elevfravar;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.vurdering.ElevfravarResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelElevfravarQueryResolver")
@Slf4j
public class ElevfravarQueryResolver {

    @Autowired
    private ElevfravarService service;

    @QueryMapping(name = "elevfravar")
    public CompletionStage<ElevfravarResource> elevfravar(
            @Argument("systemId") String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Elevfravar");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getElevfravarResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<ElevfravarResource>empty().toFuture();
    }
}
