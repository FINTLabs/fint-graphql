
package no.fint.graphql.model.model.utdanningsprogram;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.utdanningsprogram.UtdanningsprogramResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelUtdanningsprogramQueryResolver")
@Slf4j
public class UtdanningsprogramQueryResolver {

    @Autowired
    private UtdanningsprogramService service;

    @QueryMapping(name = "utdanningsprogram")
    public CompletionStage<UtdanningsprogramResource> utdanningsprogram(
            @Argument("systemId") String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Utdanningsprogram");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getUtdanningsprogramResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<UtdanningsprogramResource>empty().toFuture();
    }
}
