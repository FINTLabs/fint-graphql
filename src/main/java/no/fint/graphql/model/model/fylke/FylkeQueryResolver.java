
package no.fint.graphql.model.model.fylke;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.felles.kodeverk.FylkeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelFylkeQueryResolver")
@Slf4j
public class FylkeQueryResolver {

    @Autowired
    private FylkeService service;

    public CompletionStage<FylkeResource> fylke(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Fylke");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getFylkeResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<FylkeResource>empty().toFuture();
    }
}
