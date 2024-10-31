
package no.fint.graphql.model.utdanning.basisgruppemedlemskap;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.elev.BasisgruppemedlemskapResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningBasisgruppemedlemskapQueryResolver")
public class BasisgruppemedlemskapQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private BasisgruppemedlemskapService service;

    public CompletionStage<BasisgruppemedlemskapResource> getBasisgruppemedlemskap(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getBasisgruppemedlemskapResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<BasisgruppemedlemskapResource>empty().toFuture();
    }
}
