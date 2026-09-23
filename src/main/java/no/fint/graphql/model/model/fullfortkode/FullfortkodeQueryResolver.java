
package no.fint.graphql.model.model.fullfortkode;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.kodeverk.FullfortkodeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelFullfortkodeQueryResolver")
@Slf4j
public class FullfortkodeQueryResolver {

    @Autowired
    private FullfortkodeService service;

    public CompletionStage<FullfortkodeResource> fullfortkode(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Fullfortkode");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getFullfortkodeResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<FullfortkodeResource>empty().toFuture();
    }
}
