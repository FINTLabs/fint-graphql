
package no.fint.graphql.model.model.skoleeiertype;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.kodeverk.SkoleeiertypeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelSkoleeiertypeQueryResolver")
@Slf4j
public class SkoleeiertypeQueryResolver {

    @Autowired
    private SkoleeiertypeService service;

    public CompletionStage<SkoleeiertypeResource> skoleeiertype(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Skoleeiertype");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getSkoleeiertypeResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<SkoleeiertypeResource>empty().toFuture();
    }
}
