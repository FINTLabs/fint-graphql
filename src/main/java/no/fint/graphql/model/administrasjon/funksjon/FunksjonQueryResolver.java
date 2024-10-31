
package no.fint.graphql.model.administrasjon.funksjon;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.administrasjon.kodeverk.FunksjonResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("administrasjonFunksjonQueryResolver")
public class FunksjonQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private FunksjonService service;

    public CompletionStage<FunksjonResource> getFunksjon(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getFunksjonResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<FunksjonResource>empty().toFuture();
    }
}