
package no.fint.graphql.model.model.prosjektart;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.administrasjon.kodeverk.ProsjektartResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelProsjektartQueryResolver")
@Slf4j
public class ProsjektartQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private ProsjektartService service;

    public CompletionStage<ProsjektartResource> getProsjektart(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Prosjektart");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getProsjektartResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<ProsjektartResource>empty().toFuture();
    }
}
