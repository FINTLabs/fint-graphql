
package no.fint.graphql.model.model.kjonn;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.felles.kodeverk.iso.KjonnResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelKjonnQueryResolver")
@Slf4j
public class KjonnQueryResolver {

    @Autowired
    private KjonnService service;

    public CompletionStage<KjonnResource> kjonn(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Kjonn");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getKjonnResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<KjonnResource>empty().toFuture();
    }
}
