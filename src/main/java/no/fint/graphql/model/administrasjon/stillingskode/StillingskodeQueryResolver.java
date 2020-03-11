
package no.fint.graphql.model.administrasjon.stillingskode;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.administrasjon.kodeverk.StillingskodeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("administrasjonStillingskodeQueryResolver")
public class StillingskodeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private StillingskodeService service;

    public CompletionStage<StillingskodeResource> getStillingskode(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getStillingskodeResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<StillingskodeResource>empty().toFuture();
    }
}
