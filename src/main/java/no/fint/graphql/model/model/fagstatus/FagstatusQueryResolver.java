
package no.fint.graphql.model.model.fagstatus;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.kodeverk.FagstatusResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelFagstatusQueryResolver")
@Slf4j
public class FagstatusQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private FagstatusService service;

    public CompletionStage<FagstatusResource> getFagstatus(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Fagstatus");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getFagstatusResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<FagstatusResource>empty().toFuture();
    }
}
