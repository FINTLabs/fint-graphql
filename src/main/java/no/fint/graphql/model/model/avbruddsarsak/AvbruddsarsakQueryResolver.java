
package no.fint.graphql.model.model.avbruddsarsak;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.kodeverk.AvbruddsarsakResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelAvbruddsarsakQueryResolver")
@Slf4j
public class AvbruddsarsakQueryResolver {

    @Autowired
    private AvbruddsarsakService service;

    public CompletionStage<AvbruddsarsakResource> avbruddsarsak(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Avbruddsarsak");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getAvbruddsarsakResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<AvbruddsarsakResource>empty().toFuture();
    }
}
