
package no.fint.graphql.model.model.formal;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.administrasjon.kodeverk.FormalResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelFormalQueryResolver")
@Slf4j
public class FormalQueryResolver {

    @Autowired
    private FormalService service;

    public CompletionStage<FormalResource> formal(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Formal");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getFormalResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<FormalResource>empty().toFuture();
    }
}
