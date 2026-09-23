
package no.fint.graphql.model.model.lonnsart;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.administrasjon.kodeverk.LonnsartResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelLonnsartQueryResolver")
@Slf4j
public class LonnsartQueryResolver {

    @Autowired
    private LonnsartService service;

    public CompletionStage<LonnsartResource> lonnsart(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Lonnsart");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getLonnsartResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<LonnsartResource>empty().toFuture();
    }
}
