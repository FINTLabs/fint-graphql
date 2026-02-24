
package no.fint.graphql.model.model.kjonn;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.felles.kodeverk.iso.KjonnResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelKjonnQueryResolver")
@Slf4j
public class KjonnQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private KjonnService service;

    public CompletionStage<KjonnResource> kjonn(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Kjonn");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getKjonnResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<KjonnResource>empty().toFuture();
    }
}
