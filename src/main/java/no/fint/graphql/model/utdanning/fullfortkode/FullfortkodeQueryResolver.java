
package no.fint.graphql.model.utdanning.fullfortkode;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.utdanning.kodeverk.FullfortkodeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningFullfortkodeQueryResolver")
@Slf4j
public class FullfortkodeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private FullfortkodeService service;

    public CompletionStage<FullfortkodeResource> getFullfortkode(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Fullfortkode");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getFullfortkodeResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<FullfortkodeResource>empty().toFuture();
    }
}
