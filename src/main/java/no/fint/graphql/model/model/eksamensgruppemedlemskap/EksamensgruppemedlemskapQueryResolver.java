
package no.fint.graphql.model.model.eksamensgruppemedlemskap;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.vurdering.EksamensgruppemedlemskapResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelEksamensgruppemedlemskapQueryResolver")
@Slf4j
public class EksamensgruppemedlemskapQueryResolver {

    @Autowired
    private EksamensgruppemedlemskapService service;

    @QueryMapping(name = "eksamensgruppemedlemskap")
    public CompletionStage<EksamensgruppemedlemskapResource> eksamensgruppemedlemskap(
            @Argument("systemId") String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Eksamensgruppemedlemskap");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getEksamensgruppemedlemskapResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<EksamensgruppemedlemskapResource>empty().toFuture();
    }
}
