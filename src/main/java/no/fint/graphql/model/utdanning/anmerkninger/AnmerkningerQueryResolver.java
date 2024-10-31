
package no.fint.graphql.model.utdanning.anmerkninger;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.vurdering.AnmerkningerResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningAnmerkningerQueryResolver")
public class AnmerkningerQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private AnmerkningerService service;

    public CompletionStage<AnmerkningerResource> getAnmerkninger(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getAnmerkningerResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<AnmerkningerResource>empty().toFuture();
    }
}
