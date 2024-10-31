
package no.fint.graphql.model.utdanning.betalingsstatus;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.kodeverk.BetalingsstatusResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningBetalingsstatusQueryResolver")
public class BetalingsstatusQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private BetalingsstatusService service;

    public CompletionStage<BetalingsstatusResource> getBetalingsstatus(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getBetalingsstatusResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<BetalingsstatusResource>empty().toFuture();
    }
}
