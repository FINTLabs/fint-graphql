
package no.fint.graphql.model.administrasjon.diverse;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.administrasjon.kodeverk.DiverseResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("administrasjonDiverseQueryResolver")
@Slf4j
public class DiverseQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private DiverseService service;

    public CompletionStage<DiverseResource> getDiverse(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Diverse");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getDiverseResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<DiverseResource>empty().toFuture();
    }
}
