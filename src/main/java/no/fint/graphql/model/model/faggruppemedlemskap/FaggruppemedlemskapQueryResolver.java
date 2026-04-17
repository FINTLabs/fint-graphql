
package no.fint.graphql.model.model.faggruppemedlemskap;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.timeplan.FaggruppemedlemskapResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelFaggruppemedlemskapQueryResolver")
@Slf4j
public class FaggruppemedlemskapQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private FaggruppemedlemskapService service;

    public CompletionStage<FaggruppemedlemskapResource> getFaggruppemedlemskap(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Faggruppemedlemskap");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getFaggruppemedlemskapResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<FaggruppemedlemskapResource>empty().toFuture();
    }
}
