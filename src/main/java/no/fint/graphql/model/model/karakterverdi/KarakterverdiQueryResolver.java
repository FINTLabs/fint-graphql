
package no.fint.graphql.model.model.karakterverdi;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.vurdering.KarakterverdiResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelKarakterverdiQueryResolver")
@Slf4j
public class KarakterverdiQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private KarakterverdiService service;

    public CompletionStage<KarakterverdiResource> karakterverdi(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Karakterverdi");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getKarakterverdiResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<KarakterverdiResource>empty().toFuture();
    }
}
