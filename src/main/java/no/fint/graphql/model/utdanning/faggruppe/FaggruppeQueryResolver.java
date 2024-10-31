
package no.fint.graphql.model.utdanning.faggruppe;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.timeplan.FaggruppeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningFaggruppeQueryResolver")
public class FaggruppeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private FaggruppeService service;

    public CompletionStage<FaggruppeResource> getFaggruppe(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getFaggruppeResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<FaggruppeResource>empty().toFuture();
    }
}
