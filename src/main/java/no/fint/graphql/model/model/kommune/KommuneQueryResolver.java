
package no.fint.graphql.model.model.kommune;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.felles.kodeverk.KommuneResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelKommuneQueryResolver")
@Slf4j
public class KommuneQueryResolver {

    @Autowired
    private KommuneService service;

    public CompletionStage<KommuneResource> kommune(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Kommune");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getKommuneResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<KommuneResource>empty().toFuture();
    }
}
