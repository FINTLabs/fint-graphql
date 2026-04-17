
package no.fint.graphql.model.model.varsel;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.elev.VarselResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelVarselQueryResolver")
@Slf4j
public class VarselQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private VarselService service;

    public CompletionStage<VarselResource> getVarsel(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Varsel");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getVarselResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<VarselResource>empty().toFuture();
    }
}
