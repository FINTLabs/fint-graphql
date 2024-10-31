
package no.fint.graphql.model.utdanning.provestatus;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.kodeverk.ProvestatusResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningProvestatusQueryResolver")
public class ProvestatusQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private ProvestatusService service;

    public CompletionStage<ProvestatusResource> getProvestatus(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getProvestatusResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<ProvestatusResource>empty().toFuture();
    }
}
