
package no.fint.graphql.model.utdanning.fag;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.utdanning.timeplan.FagResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningFagQueryResolver")
@Slf4j
public class FagQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private FagService service;

    public CompletionStage<FagResource> getFag(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Fag");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getFagResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<FagResource>empty().toFuture();
    }
}
