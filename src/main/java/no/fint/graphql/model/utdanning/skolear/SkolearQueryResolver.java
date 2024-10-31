
package no.fint.graphql.model.utdanning.skolear;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.kodeverk.SkolearResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningSkolearQueryResolver")
public class SkolearQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private SkolearService service;

    public CompletionStage<SkolearResource> getSkolear(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getSkolearResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<SkolearResource>empty().toFuture();
    }
}
