
package no.fint.graphql.model.model.provestatus;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.kodeverk.ProvestatusResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelProvestatusQueryResolver")
@Slf4j
public class ProvestatusQueryResolver {

    @Autowired
    private ProvestatusService service;

    public CompletionStage<ProvestatusResource> provestatus(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Provestatus");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getProvestatusResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<ProvestatusResource>empty().toFuture();
    }
}
