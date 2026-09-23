
package no.fint.graphql.model.model.karakterhistorie;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.vurdering.KarakterhistorieResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelKarakterhistorieQueryResolver")
@Slf4j
public class KarakterhistorieQueryResolver {

    @Autowired
    private KarakterhistorieService service;

    @QueryMapping(name = "karakterhistorie")
    public CompletionStage<KarakterhistorieResource> karakterhistorie(
            @Argument("systemId") String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Karakterhistorie");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getKarakterhistorieResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<KarakterhistorieResource>empty().toFuture();
    }
}
