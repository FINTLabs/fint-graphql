
package no.fint.graphql.model.model.fravarsoversikt;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.vurdering.FravarsoversiktResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelFravarsoversiktQueryResolver")
@Slf4j
public class FravarsoversiktQueryResolver {

    @Autowired
    private FravarsoversiktService service;

    @QueryMapping(name = "fravarsoversikt")
    public CompletionStage<FravarsoversiktResource> fravarsoversikt(
            @Argument("systemId") String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Fravarsoversikt");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getFravarsoversiktResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<FravarsoversiktResource>empty().toFuture();
    }
}
