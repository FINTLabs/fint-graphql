
package no.fint.graphql.model.administrasjon.diverse;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.administrasjon.kodeverk.DiverseResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("administrasjonDiverseQueryResolver")
public class DiverseQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private DiverseService service;

    public CompletionStage<DiverseResource> getDiverse(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getDiverseResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<DiverseResource>empty().toFuture();
    }
}
