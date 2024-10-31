
package no.fint.graphql.model.utdanning.avbruddsarsak;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.kodeverk.AvbruddsarsakResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningAvbruddsarsakQueryResolver")
public class AvbruddsarsakQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private AvbruddsarsakService service;

    public CompletionStage<AvbruddsarsakResource> getAvbruddsarsak(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getAvbruddsarsakResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<AvbruddsarsakResource>empty().toFuture();
    }
}