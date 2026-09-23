
package no.fint.graphql.model.model.faggruppemedlemskap;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.timeplan.FaggruppemedlemskapResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelFaggruppemedlemskapQueryResolver")
@Slf4j
public class FaggruppemedlemskapQueryResolver {

    @Autowired
    private FaggruppemedlemskapService service;

    @QueryMapping(name = "faggruppemedlemskap")
    public CompletionStage<FaggruppemedlemskapResource> faggruppemedlemskap(
            @Argument("systemId") String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Faggruppemedlemskap");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getFaggruppemedlemskapResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<FaggruppemedlemskapResource>empty().toFuture();
    }
}
