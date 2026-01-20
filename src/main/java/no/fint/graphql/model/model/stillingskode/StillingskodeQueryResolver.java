
package no.fint.graphql.model.model.stillingskode;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.administrasjon.kodeverk.StillingskodeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelStillingskodeQueryResolver")
@Slf4j
public class StillingskodeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private StillingskodeService service;

    public CompletionStage<StillingskodeResource> getStillingskode(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Stillingskode");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getStillingskodeResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<StillingskodeResource>empty().toFuture();
    }
}
