
package no.fint.graphql.model.model.kontaktlarergruppemedlemskap;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.elev.KontaktlarergruppemedlemskapResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelKontaktlarergruppemedlemskapQueryResolver")
@Slf4j
public class KontaktlarergruppemedlemskapQueryResolver {

    @Autowired
    private KontaktlarergruppemedlemskapService service;

    @QueryMapping(name = "kontaktlarergruppemedlemskap")
    public CompletionStage<KontaktlarergruppemedlemskapResource> kontaktlarergruppemedlemskap(
            @Argument("systemId") String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Kontaktlarergruppemedlemskap");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getKontaktlarergruppemedlemskapResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<KontaktlarergruppemedlemskapResource>empty().toFuture();
    }
}
