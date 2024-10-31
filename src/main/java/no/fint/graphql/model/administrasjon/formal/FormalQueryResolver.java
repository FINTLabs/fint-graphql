
package no.fint.graphql.model.administrasjon.formal;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.administrasjon.kodeverk.FormalResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("administrasjonFormalQueryResolver")
public class FormalQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private FormalService service;

    public CompletionStage<FormalResource> getFormal(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getFormalResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<FormalResource>empty().toFuture();
    }
}
