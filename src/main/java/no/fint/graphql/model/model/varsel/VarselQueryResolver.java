
package no.fint.graphql.model.model.varsel;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.elev.VarselResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelVarselQueryResolver")
@Slf4j
public class VarselQueryResolver {

    @Autowired
    private VarselService service;

    @QueryMapping(name = "varsel")
    public CompletionStage<VarselResource> varsel(
            @Argument("systemId") String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Varsel");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getVarselResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<VarselResource>empty().toFuture();
    }
}
