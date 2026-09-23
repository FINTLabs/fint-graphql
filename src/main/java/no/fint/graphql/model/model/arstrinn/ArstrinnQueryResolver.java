
package no.fint.graphql.model.model.arstrinn;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.utdanningsprogram.ArstrinnResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelArstrinnQueryResolver")
@Slf4j
public class ArstrinnQueryResolver {

    @Autowired
    private ArstrinnService service;

    @QueryMapping(name = "arstrinn")
    public CompletionStage<ArstrinnResource> arstrinn(
            @Argument("systemId") String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Arstrinn");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getArstrinnResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<ArstrinnResource>empty().toFuture();
    }
}
