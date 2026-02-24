
package no.fint.graphql.model.model.undervisningsgruppe;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelUndervisningsgruppeQueryResolver")
@Slf4j
public class UndervisningsgruppeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private UndervisningsgruppeService service;

    public CompletionStage<UndervisningsgruppeResource> undervisningsgruppe(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Undervisningsgruppe");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getUndervisningsgruppeResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<UndervisningsgruppeResource>empty().toFuture();
    }
}
