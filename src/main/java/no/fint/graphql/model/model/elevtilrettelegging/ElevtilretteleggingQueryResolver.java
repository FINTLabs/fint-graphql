
package no.fint.graphql.model.model.elevtilrettelegging;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.elev.ElevtilretteleggingResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelElevtilretteleggingQueryResolver")
@Slf4j
public class ElevtilretteleggingQueryResolver {

    @Autowired
    private ElevtilretteleggingService service;

    @QueryMapping(name = "elevtilrettelegging")
    public CompletionStage<ElevtilretteleggingResource> elevtilrettelegging(
            @Argument("systemId") String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Elevtilrettelegging");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getElevtilretteleggingResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<ElevtilretteleggingResource>empty().toFuture();
    }
}
