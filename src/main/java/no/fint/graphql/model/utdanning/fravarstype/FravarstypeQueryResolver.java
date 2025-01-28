
package no.fint.graphql.model.utdanning.fravarstype;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.utdanning.kodeverk.FravarstypeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningFravarstypeQueryResolver")
@Slf4j
public class FravarstypeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private FravarstypeService service;

    public CompletionStage<FravarstypeResource> getFravarstype(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Fravarstype");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getFravarstypeResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<FravarstypeResource>empty().toFuture();
    }
}
