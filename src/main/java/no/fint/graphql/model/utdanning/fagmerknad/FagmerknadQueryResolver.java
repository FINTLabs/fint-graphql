
package no.fint.graphql.model.utdanning.fagmerknad;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.utdanning.kodeverk.FagmerknadResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningFagmerknadQueryResolver")
@Slf4j
public class FagmerknadQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private FagmerknadService service;

    public CompletionStage<FagmerknadResource> getFagmerknad(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Fagmerknad");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getFagmerknadResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<FagmerknadResource>empty().toFuture();
    }
}
