
package no.fint.graphql.model.model.fag;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.timeplan.FagResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelFagQueryResolver")
@Slf4j
public class FagQueryResolver {

    @Autowired
    private FagService service;

    @QueryMapping(name = "fag")
    public CompletionStage<FagResource> fag(
            @Argument("systemId") String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Fag");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getFagResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<FagResource>empty().toFuture();
    }
}
