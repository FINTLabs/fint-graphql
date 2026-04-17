
package no.fint.graphql.model.model.fravarsregistrering;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.vurdering.FravarsregistreringResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelFravarsregistreringQueryResolver")
@Slf4j
public class FravarsregistreringQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private FravarsregistreringService service;

    public CompletionStage<FravarsregistreringResource> getFravarsregistrering(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Fravarsregistrering");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getFravarsregistreringResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<FravarsregistreringResource>empty().toFuture();
    }
}
