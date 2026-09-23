
package no.fint.graphql.model.model.karakterstatus;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.kodeverk.KarakterstatusResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelKarakterstatusQueryResolver")
@Slf4j
public class KarakterstatusQueryResolver {

    @Autowired
    private KarakterstatusService service;

    public CompletionStage<KarakterstatusResource> karakterstatus(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Karakterstatus");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getKarakterstatusResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<KarakterstatusResource>empty().toFuture();
    }
}
