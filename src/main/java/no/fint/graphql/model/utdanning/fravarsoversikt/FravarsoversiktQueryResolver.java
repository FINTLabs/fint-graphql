
package no.fint.graphql.model.utdanning.fravarsoversikt;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.vurdering.FravarsoversiktResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningFravarsoversiktQueryResolver")
public class FravarsoversiktQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private FravarsoversiktService service;

    public CompletionStage<FravarsoversiktResource> getFravarsoversikt(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getFravarsoversiktResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<FravarsoversiktResource>empty().toFuture();
    }
}
