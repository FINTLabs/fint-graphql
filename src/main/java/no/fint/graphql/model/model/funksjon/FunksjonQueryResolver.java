
package no.fint.graphql.model.model.funksjon;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.administrasjon.kodeverk.FunksjonResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelFunksjonQueryResolver")
@Slf4j
public class FunksjonQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private FunksjonService service;

    public CompletionStage<FunksjonResource> funksjon(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Funksjon");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getFunksjonResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<FunksjonResource>empty().toFuture();
    }
}
