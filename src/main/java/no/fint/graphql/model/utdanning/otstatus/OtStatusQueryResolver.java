
package no.fint.graphql.model.utdanning.otstatus;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.kodeverk.OtStatusResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningOtStatusQueryResolver")
public class OtStatusQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private OtStatusService service;

    public CompletionStage<OtStatusResource> getOtStatus(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getOtStatusResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<OtStatusResource>empty().toFuture();
    }
}
