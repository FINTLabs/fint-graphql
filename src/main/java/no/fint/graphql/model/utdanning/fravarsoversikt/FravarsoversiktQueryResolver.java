
package no.fint.graphql.model.utdanning.fravarsoversikt;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.utdanning.vurdering.FravarsoversiktResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningFravarsoversiktQueryResolver")
@Slf4j
public class FravarsoversiktQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private FravarsoversiktService service;

    public CompletionStage<FravarsoversiktResource> getFravarsoversikt(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Fravarsoversikt");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getFravarsoversiktResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<FravarsoversiktResource>empty().toFuture();
    }
}
