
package no.fint.graphql.model.utdanning.fagstatus;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.kodeverk.FagstatusResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningFagstatusQueryResolver")
public class FagstatusQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private FagstatusService service;

    public CompletionStage<FagstatusResource> getFagstatus(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getFagstatusResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<FagstatusResource>empty().toFuture();
    }
}
