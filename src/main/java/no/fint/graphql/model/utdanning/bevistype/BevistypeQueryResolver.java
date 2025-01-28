
package no.fint.graphql.model.utdanning.bevistype;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.utdanning.kodeverk.BevistypeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningBevistypeQueryResolver")
@Slf4j
public class BevistypeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private BevistypeService service;

    public CompletionStage<BevistypeResource> getBevistype(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Bevistype");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getBevistypeResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<BevistypeResource>empty().toFuture();
    }
}
