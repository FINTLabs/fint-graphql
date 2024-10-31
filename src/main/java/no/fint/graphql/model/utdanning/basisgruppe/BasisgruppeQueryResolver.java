
package no.fint.graphql.model.utdanning.basisgruppe;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.elev.BasisgruppeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningBasisgruppeQueryResolver")
public class BasisgruppeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private BasisgruppeService service;

    public CompletionStage<BasisgruppeResource> getBasisgruppe(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getBasisgruppeResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<BasisgruppeResource>empty().toFuture();
    }
}
