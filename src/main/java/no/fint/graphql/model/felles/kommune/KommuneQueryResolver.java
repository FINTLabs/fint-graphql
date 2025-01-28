
package no.fint.graphql.model.felles.kommune;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.felles.kodeverk.KommuneResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("fellesKommuneQueryResolver")
@Slf4j
public class KommuneQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private KommuneService service;

    public CompletionStage<KommuneResource> getKommune(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Kommune");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getKommuneResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<KommuneResource>empty().toFuture();
    }
}
