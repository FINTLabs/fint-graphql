
package no.fint.graphql.model.utdanning.varsel;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.elev.VarselResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningVarselQueryResolver")
public class VarselQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private VarselService service;

    public CompletionStage<VarselResource> getVarsel(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getVarselResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<VarselResource>empty().toFuture();
    }
}
