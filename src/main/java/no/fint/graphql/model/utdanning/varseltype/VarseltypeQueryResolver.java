
package no.fint.graphql.model.utdanning.varseltype;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.kodeverk.VarseltypeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningVarseltypeQueryResolver")
public class VarseltypeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private VarseltypeService service;

    public CompletionStage<VarseltypeResource> getVarseltype(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getVarseltypeResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<VarseltypeResource>empty().toFuture();
    }
}
