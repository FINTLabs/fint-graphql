
package no.fint.graphql.model.utdanning.brevtype;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.utdanning.kodeverk.BrevtypeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningBrevtypeQueryResolver")
@Slf4j
public class BrevtypeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private BrevtypeService service;

    public CompletionStage<BrevtypeResource> getBrevtype(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Brevtype");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getBrevtypeResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<BrevtypeResource>empty().toFuture();
    }
}
