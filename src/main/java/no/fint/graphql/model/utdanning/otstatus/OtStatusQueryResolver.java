
package no.fint.graphql.model.utdanning.otstatus;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.utdanning.kodeverk.OtStatusResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningOtStatusQueryResolver")
@Slf4j
public class OtStatusQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private OtStatusService service;

    public CompletionStage<OtStatusResource> getOtStatus(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for OtStatus");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getOtStatusResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<OtStatusResource>empty().toFuture();
    }
}
