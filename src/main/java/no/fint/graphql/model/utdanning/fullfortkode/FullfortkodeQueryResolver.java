
package no.fint.graphql.model.utdanning.fullfortkode;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.kodeverk.FullfortkodeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningFullfortkodeQueryResolver")
public class FullfortkodeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private FullfortkodeService service;

    public CompletionStage<FullfortkodeResource> getFullfortkode(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getFullfortkodeResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<FullfortkodeResource>empty().toFuture();
    }
}
