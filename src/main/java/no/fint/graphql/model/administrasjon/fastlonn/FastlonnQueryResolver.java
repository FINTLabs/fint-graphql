
package no.fint.graphql.model.administrasjon.fastlonn;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.administrasjon.personal.FastlonnResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("administrasjonFastlonnQueryResolver")
@Slf4j
public class FastlonnQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private FastlonnService service;

    public CompletionStage<FastlonnResource> getFastlonn(
            String kildesystemId,
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Fastlonn");
        if (StringUtils.isNotEmpty(kildesystemId)) {
            return service.getFastlonnResourceById("kildesystemid", kildesystemId, dfe).toFuture();
        }
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getFastlonnResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<FastlonnResource>empty().toFuture();
    }
}
