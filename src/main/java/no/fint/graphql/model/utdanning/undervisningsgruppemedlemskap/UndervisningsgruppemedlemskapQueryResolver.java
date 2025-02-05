
package no.fint.graphql.model.utdanning.undervisningsgruppemedlemskap;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.utdanning.timeplan.UndervisningsgruppemedlemskapResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningUndervisningsgruppemedlemskapQueryResolver")
@Slf4j
public class UndervisningsgruppemedlemskapQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private UndervisningsgruppemedlemskapService service;

    public CompletionStage<UndervisningsgruppemedlemskapResource> getUndervisningsgruppemedlemskap(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Undervisningsgruppemedlemskap");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getUndervisningsgruppemedlemskapResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<UndervisningsgruppemedlemskapResource>empty().toFuture();
    }
}
