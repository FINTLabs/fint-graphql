
package no.fint.graphql.model.utdanning.arstrinn;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.utdanningsprogram.ArstrinnResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningArstrinnQueryResolver")
public class ArstrinnQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private ArstrinnService service;

    public CompletionStage<ArstrinnResource> getArstrinn(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getArstrinnResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<ArstrinnResource>empty().toFuture();
    }
}
