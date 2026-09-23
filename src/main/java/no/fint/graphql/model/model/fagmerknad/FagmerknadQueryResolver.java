
package no.fint.graphql.model.model.fagmerknad;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.kodeverk.FagmerknadResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelFagmerknadQueryResolver")
@Slf4j
public class FagmerknadQueryResolver {

    @Autowired
    private FagmerknadService service;

    public CompletionStage<FagmerknadResource> fagmerknad(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Fagmerknad");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getFagmerknadResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<FagmerknadResource>empty().toFuture();
    }
}
