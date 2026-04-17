
package no.fint.graphql.model.model.fullmakt;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.administrasjon.fullmakt.FullmaktResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelFullmaktQueryResolver")
@Slf4j
public class FullmaktQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private FullmaktService service;

    public CompletionStage<FullmaktResource> getFullmakt(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Fullmakt");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getFullmaktResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<FullmaktResource>empty().toFuture();
    }
}
