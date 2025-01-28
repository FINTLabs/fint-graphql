
package no.fint.graphql.model.utdanning.skoleeiertype;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.utdanning.kodeverk.SkoleeiertypeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningSkoleeiertypeQueryResolver")
@Slf4j
public class SkoleeiertypeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private SkoleeiertypeService service;

    public CompletionStage<SkoleeiertypeResource> getSkoleeiertype(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Skoleeiertype");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getSkoleeiertypeResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<SkoleeiertypeResource>empty().toFuture();
    }
}
