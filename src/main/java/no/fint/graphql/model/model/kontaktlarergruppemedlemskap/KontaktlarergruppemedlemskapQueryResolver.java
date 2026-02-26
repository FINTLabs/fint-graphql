
package no.fint.graphql.model.model.kontaktlarergruppemedlemskap;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.elev.KontaktlarergruppemedlemskapResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelKontaktlarergruppemedlemskapQueryResolver")
@Slf4j
public class KontaktlarergruppemedlemskapQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private KontaktlarergruppemedlemskapService service;

    public CompletionStage<KontaktlarergruppemedlemskapResource> kontaktlarergruppemedlemskap(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Kontaktlarergruppemedlemskap");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getKontaktlarergruppemedlemskapResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<KontaktlarergruppemedlemskapResource>empty().toFuture();
    }
}
