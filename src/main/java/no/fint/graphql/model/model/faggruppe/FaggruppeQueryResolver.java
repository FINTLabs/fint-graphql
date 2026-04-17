
package no.fint.graphql.model.model.faggruppe;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.timeplan.FaggruppeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelFaggruppeQueryResolver")
@Slf4j
public class FaggruppeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private FaggruppeService service;

    public CompletionStage<FaggruppeResource> getFaggruppe(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Faggruppe");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getFaggruppeResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<FaggruppeResource>empty().toFuture();
    }
}
