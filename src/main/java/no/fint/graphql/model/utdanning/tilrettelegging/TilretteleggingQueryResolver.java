
package no.fint.graphql.model.utdanning.tilrettelegging;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.utdanning.kodeverk.TilretteleggingResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningTilretteleggingQueryResolver")
@Slf4j
public class TilretteleggingQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private TilretteleggingService service;

    public CompletionStage<TilretteleggingResource> getTilrettelegging(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Tilrettelegging");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getTilretteleggingResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<TilretteleggingResource>empty().toFuture();
    }
}
