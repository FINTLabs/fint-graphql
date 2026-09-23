
package no.fint.graphql.model.model.diverse;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.administrasjon.kodeverk.DiverseResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelDiverseQueryResolver")
@Slf4j
public class DiverseQueryResolver {

    @Autowired
    private DiverseService service;

    public CompletionStage<DiverseResource> diverse(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Diverse");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getDiverseResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<DiverseResource>empty().toFuture();
    }
}
