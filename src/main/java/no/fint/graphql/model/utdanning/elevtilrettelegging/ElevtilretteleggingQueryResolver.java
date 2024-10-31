
package no.fint.graphql.model.utdanning.elevtilrettelegging;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.elev.ElevtilretteleggingResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningElevtilretteleggingQueryResolver")
public class ElevtilretteleggingQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private ElevtilretteleggingService service;

    public CompletionStage<ElevtilretteleggingResource> getElevtilrettelegging(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getElevtilretteleggingResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<ElevtilretteleggingResource>empty().toFuture();
    }
}
