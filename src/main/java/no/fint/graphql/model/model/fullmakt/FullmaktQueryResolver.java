
package no.fint.graphql.model.model.fullmakt;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.administrasjon.fullmakt.FullmaktResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelFullmaktQueryResolver")
@Slf4j
public class FullmaktQueryResolver {

    @Autowired
    private FullmaktService service;

    @QueryMapping(name = "fullmakt")
    public CompletionStage<FullmaktResource> fullmakt(
            @Argument("systemId") String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Fullmakt");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getFullmaktResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<FullmaktResource>empty().toFuture();
    }
}
