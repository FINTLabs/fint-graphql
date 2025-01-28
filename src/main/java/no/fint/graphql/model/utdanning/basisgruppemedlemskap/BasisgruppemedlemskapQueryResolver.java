
package no.fint.graphql.model.utdanning.basisgruppemedlemskap;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.utdanning.elev.BasisgruppemedlemskapResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningBasisgruppemedlemskapQueryResolver")
@Slf4j
public class BasisgruppemedlemskapQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private BasisgruppemedlemskapService service;

    public CompletionStage<BasisgruppemedlemskapResource> getBasisgruppemedlemskap(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Basisgruppemedlemskap");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getBasisgruppemedlemskapResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<BasisgruppemedlemskapResource>empty().toFuture();
    }
}
