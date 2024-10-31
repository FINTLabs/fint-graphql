
package no.fint.graphql.model.administrasjon.variabellonn;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.administrasjon.personal.VariabellonnResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("administrasjonVariabellonnQueryResolver")
public class VariabellonnQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private VariabellonnService service;

    public CompletionStage<VariabellonnResource> getVariabellonn(
            String kildesystemId,
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(kildesystemId)) {
            return service.getVariabellonnResourceById("kildesystemid", kildesystemId, dfe).toFuture();
        }
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getVariabellonnResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<VariabellonnResource>empty().toFuture();
    }
}