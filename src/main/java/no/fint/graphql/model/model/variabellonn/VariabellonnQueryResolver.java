
package no.fint.graphql.model.model.variabellonn;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.administrasjon.personal.VariabellonnResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelVariabellonnQueryResolver")
@Slf4j
public class VariabellonnQueryResolver {

    @Autowired
    private VariabellonnService service;

    public CompletionStage<VariabellonnResource> variabellonn(
            String kildesystemId,
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Variabellonn");
        if (StringUtils.isNotEmpty(kildesystemId)) {
            return service.getVariabellonnResourceById("kildesystemid", kildesystemId, dfe).toFuture();
        }
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getVariabellonnResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<VariabellonnResource>empty().toFuture();
    }
}
