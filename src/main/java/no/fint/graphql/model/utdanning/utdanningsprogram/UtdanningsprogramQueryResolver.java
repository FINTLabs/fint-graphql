
package no.fint.graphql.model.utdanning.utdanningsprogram;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.utdanningsprogram.UtdanningsprogramResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningUtdanningsprogramQueryResolver")
public class UtdanningsprogramQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private UtdanningsprogramService service;

    public CompletionStage<UtdanningsprogramResource> getUtdanningsprogram(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getUtdanningsprogramResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<UtdanningsprogramResource>empty().toFuture();
    }
}
