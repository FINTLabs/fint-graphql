
package no.fint.graphql.model.model.karakterverdi;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.vurdering.KarakterverdiResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelKarakterverdiQueryResolver")
@Slf4j
public class KarakterverdiQueryResolver {

    @Autowired
    private KarakterverdiService service;

    @QueryMapping(name = "karakterverdi")
    public CompletionStage<KarakterverdiResource> karakterverdi(
            @Argument("systemId") String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Karakterverdi");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getKarakterverdiResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<KarakterverdiResource>empty().toFuture();
    }
}
