
package no.fint.graphql.model.model.anmerkninger;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.vurdering.AnmerkningerResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelAnmerkningerQueryResolver")
@Slf4j
public class AnmerkningerQueryResolver {

    @Autowired
    private AnmerkningerService service;

    @QueryMapping(name = "anmerkninger")
    public CompletionStage<AnmerkningerResource> anmerkninger(
            @Argument("systemId") String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Anmerkninger");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getAnmerkningerResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<AnmerkningerResource>empty().toFuture();
    }
}
