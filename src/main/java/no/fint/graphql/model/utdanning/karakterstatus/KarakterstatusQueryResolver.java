
package no.fint.graphql.model.utdanning.karakterstatus;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.kodeverk.KarakterstatusResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningKarakterstatusQueryResolver")
public class KarakterstatusQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private KarakterstatusService service;

    public CompletionStage<KarakterstatusResource> getKarakterstatus(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getKarakterstatusResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<KarakterstatusResource>empty().toFuture();
    }
}
