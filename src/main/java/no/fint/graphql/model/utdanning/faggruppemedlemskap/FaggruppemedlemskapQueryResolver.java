
package no.fint.graphql.model.utdanning.faggruppemedlemskap;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.timeplan.FaggruppemedlemskapResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningFaggruppemedlemskapQueryResolver")
public class FaggruppemedlemskapQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private FaggruppemedlemskapService service;

    public CompletionStage<FaggruppemedlemskapResource> getFaggruppemedlemskap(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getFaggruppemedlemskapResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<FaggruppemedlemskapResource>empty().toFuture();
    }
}
