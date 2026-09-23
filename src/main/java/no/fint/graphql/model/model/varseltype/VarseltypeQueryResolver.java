
package no.fint.graphql.model.model.varseltype;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.kodeverk.VarseltypeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelVarseltypeQueryResolver")
@Slf4j
public class VarseltypeQueryResolver {

    @Autowired
    private VarseltypeService service;

    public CompletionStage<VarseltypeResource> varseltype(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Varseltype");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getVarseltypeResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<VarseltypeResource>empty().toFuture();
    }
}
