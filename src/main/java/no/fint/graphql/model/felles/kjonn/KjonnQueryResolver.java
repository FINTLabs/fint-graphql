
package no.fint.graphql.model.felles.kjonn;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.felles.kodeverk.iso.KjonnResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("fellesKjonnQueryResolver")
public class KjonnQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private KjonnService service;

    public CompletionStage<KjonnResource> getKjonn(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getKjonnResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<KjonnResource>empty().toFuture();
    }
}
