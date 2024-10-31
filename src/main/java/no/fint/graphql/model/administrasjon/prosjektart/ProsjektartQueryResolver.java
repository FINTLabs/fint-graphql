
package no.fint.graphql.model.administrasjon.prosjektart;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.administrasjon.kodeverk.ProsjektartResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("administrasjonProsjektartQueryResolver")
public class ProsjektartQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private ProsjektartService service;

    public CompletionStage<ProsjektartResource> getProsjektart(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getProsjektartResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<ProsjektartResource>empty().toFuture();
    }
}
