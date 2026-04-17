
package no.fint.graphql.model.model.karakterstatus;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.kodeverk.KarakterstatusResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelKarakterstatusQueryResolver")
@Slf4j
public class KarakterstatusQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private KarakterstatusService service;

    public CompletionStage<KarakterstatusResource> getKarakterstatus(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Karakterstatus");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getKarakterstatusResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<KarakterstatusResource>empty().toFuture();
    }
}
