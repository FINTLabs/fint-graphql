
package no.fint.graphql.model.model.termin;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.kodeverk.TerminResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelTerminQueryResolver")
@Slf4j
public class TerminQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private TerminService service;

    public CompletionStage<TerminResource> termin(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Termin");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getTerminResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<TerminResource>empty().toFuture();
    }
}
