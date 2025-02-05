
package no.fint.graphql.model.utdanning.time;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.utdanning.timeplan.TimeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningTimeQueryResolver")
@Slf4j
public class TimeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private TimeService service;

    public CompletionStage<TimeResource> getTime(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Time");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getTimeResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<TimeResource>empty().toFuture();
    }
}
