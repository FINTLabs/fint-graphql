
package no.fint.graphql.model.utdanning.skoleeiertype;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.kodeverk.SkoleeiertypeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningSkoleeiertypeQueryResolver")
public class SkoleeiertypeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private SkoleeiertypeService service;

    public CompletionStage<SkoleeiertypeResource> getSkoleeiertype(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getSkoleeiertypeResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<SkoleeiertypeResource>empty().toFuture();
    }
}
