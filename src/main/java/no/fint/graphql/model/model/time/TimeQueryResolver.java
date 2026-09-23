
package no.fint.graphql.model.model.time;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.timeplan.TimeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelTimeQueryResolver")
@Slf4j
public class TimeQueryResolver {

    @Autowired
    private TimeService service;

    @QueryMapping(name = "time")
    public CompletionStage<TimeResource> time(
            @Argument("systemId") String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Time");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getTimeResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<TimeResource>empty().toFuture();
    }
}
