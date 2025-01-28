
package no.fint.graphql.model.utdanning.basisgruppe;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.utdanning.elev.BasisgruppeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningBasisgruppeQueryResolver")
@Slf4j
public class BasisgruppeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private BasisgruppeService service;

    public CompletionStage<BasisgruppeResource> getBasisgruppe(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Basisgruppe");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getBasisgruppeResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<BasisgruppeResource>empty().toFuture();
    }
}
