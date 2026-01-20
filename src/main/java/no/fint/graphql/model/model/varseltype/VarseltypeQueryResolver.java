
package no.fint.graphql.model.model.varseltype;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.kodeverk.VarseltypeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelVarseltypeQueryResolver")
@Slf4j
public class VarseltypeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private VarseltypeService service;

    public CompletionStage<VarseltypeResource> getVarseltype(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Varseltype");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getVarseltypeResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<VarseltypeResource>empty().toFuture();
    }
}
