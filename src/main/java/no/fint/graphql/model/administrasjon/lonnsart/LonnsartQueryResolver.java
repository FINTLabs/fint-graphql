
package no.fint.graphql.model.administrasjon.lonnsart;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.administrasjon.kodeverk.LonnsartResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("administrasjonLonnsartQueryResolver")
@Slf4j
public class LonnsartQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private LonnsartService service;

    public CompletionStage<LonnsartResource> getLonnsart(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Lonnsart");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getLonnsartResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<LonnsartResource>empty().toFuture();
    }
}
