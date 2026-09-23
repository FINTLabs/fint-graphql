
package no.fint.graphql.model.model.fastlonn;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.administrasjon.personal.FastlonnResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelFastlonnQueryResolver")
@Slf4j
public class FastlonnQueryResolver {

    @Autowired
    private FastlonnService service;

    public CompletionStage<FastlonnResource> fastlonn(
            String kildesystemId,
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Fastlonn");
        if (StringUtils.isNotEmpty(kildesystemId)) {
            return service.getFastlonnResourceById("kildesystemid", kildesystemId, dfe).toFuture();
        }
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getFastlonnResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<FastlonnResource>empty().toFuture();
    }
}
