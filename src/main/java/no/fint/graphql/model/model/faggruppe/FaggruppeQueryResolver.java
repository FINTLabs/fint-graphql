
package no.fint.graphql.model.model.faggruppe;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.timeplan.FaggruppeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelFaggruppeQueryResolver")
@Slf4j
public class FaggruppeQueryResolver {

    @Autowired
    private FaggruppeService service;

    @QueryMapping(name = "faggruppe")
    public CompletionStage<FaggruppeResource> faggruppe(
            @Argument("systemId") String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Faggruppe");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getFaggruppeResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<FaggruppeResource>empty().toFuture();
    }
}
