
package no.fint.graphql.model.administrasjon.ramme;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.administrasjon.kodeverk.RammeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("administrasjonRammeQueryResolver")
@Slf4j
public class RammeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private RammeService service;

    public CompletionStage<RammeResource> getRamme(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Ramme");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getRammeResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<RammeResource>empty().toFuture();
    }
}
