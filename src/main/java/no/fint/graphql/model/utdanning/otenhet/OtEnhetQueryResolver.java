
package no.fint.graphql.model.utdanning.otenhet;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.kodeverk.OtEnhetResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningOtEnhetQueryResolver")
public class OtEnhetQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private OtEnhetService service;

    public CompletionStage<OtEnhetResource> getOtEnhet(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getOtEnhetResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<OtEnhetResource>empty().toFuture();
    }
}
