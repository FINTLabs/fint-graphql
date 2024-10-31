
package no.fint.graphql.model.utdanning.brevtype;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.kodeverk.BrevtypeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningBrevtypeQueryResolver")
public class BrevtypeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private BrevtypeService service;

    public CompletionStage<BrevtypeResource> getBrevtype(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getBrevtypeResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<BrevtypeResource>empty().toFuture();
    }
}
