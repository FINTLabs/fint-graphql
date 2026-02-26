
package no.fint.graphql.model.model.elevtilrettelegging;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.elev.ElevtilretteleggingResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelElevtilretteleggingQueryResolver")
@Slf4j
public class ElevtilretteleggingQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private ElevtilretteleggingService service;

    public CompletionStage<ElevtilretteleggingResource> elevtilrettelegging(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Elevtilrettelegging");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getElevtilretteleggingResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<ElevtilretteleggingResource>empty().toFuture();
    }
}
