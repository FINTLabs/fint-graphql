
package no.fint.graphql.model.utdanning.arstrinn;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.utdanning.utdanningsprogram.ArstrinnResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningArstrinnQueryResolver")
@Slf4j
public class ArstrinnQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private ArstrinnService service;

    public CompletionStage<ArstrinnResource> getArstrinn(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Arstrinn");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getArstrinnResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<ArstrinnResource>empty().toFuture();
    }
}
