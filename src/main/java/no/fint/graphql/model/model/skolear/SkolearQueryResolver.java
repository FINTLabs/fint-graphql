
package no.fint.graphql.model.model.skolear;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.kodeverk.SkolearResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelSkolearQueryResolver")
@Slf4j
public class SkolearQueryResolver {

    @Autowired
    private SkolearService service;

    public CompletionStage<SkolearResource> skolear(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Skolear");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getSkolearResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<SkolearResource>empty().toFuture();
    }
}
