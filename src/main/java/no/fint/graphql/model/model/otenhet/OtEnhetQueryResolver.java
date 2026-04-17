
package no.fint.graphql.model.model.otenhet;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.kodeverk.OtEnhetResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelOtEnhetQueryResolver")
@Slf4j
public class OtEnhetQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private OtEnhetService service;

    public CompletionStage<OtEnhetResource> getOtEnhet(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for OtEnhet");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getOtEnhetResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<OtEnhetResource>empty().toFuture();
    }
}
